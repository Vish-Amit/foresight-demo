package com.inn.foresight.core.infra.rest.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.cxf.jaxrs.ext.search.SearchContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.inn.core.generic.exceptions.application.BusinessException;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.rest.AbstractCXFRestService;
import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.infra.service.IIBSVisualisationService;
import com.inn.foresight.core.infra.wrapper.IBSSelectionLayer;

@Path("IBSVisualisationRestImpl")
@Produces("application/json")
@Consumes("application/json")
public class IBSVisualisationRestImpl extends AbstractCXFRestService<Integer, Object> {

	public IBSVisualisationRestImpl() {
		super(Object.class);
	}

	@Autowired
	private IIBSVisualisationService iIBSVisualisationService;
	private Logger logger = LogManager.getLogger(SiteVisualisationRestImpl.class);

	@Override
	public Object create(@Valid Object arg0) throws RestException, BusinessException {
		return new Object();
	}

	@Override
	public List<Object> findAll() throws RestException {
		return new ArrayList<>();
	}

	@Override
	public Object findById(@NotNull Integer arg0) throws RestException {
		return new Object();
	}

	@Override
	public boolean remove(@Valid Object arg0) throws RestException {

		return false;
	}

	@Override
	public void removeById(@NotNull Integer arg0) throws RestException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Object> search(Object arg0) throws RestException {
		// TODO Auto-generated method stub
		return new ArrayList<>();
	}

	@Override
	public Object update(@Valid Object arg0) throws RestException {
		// TODO Auto-generated method stub
		return new Object();
	}

	@Override
	public SearchContext getSearchContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IGenericService<Integer, Object> getService() {
		// TODO Auto-generated method stub
		return null;
	}

	@POST
	@Path("getIBSDetail")
	public List<Map> getIBSDetail(IBSSelectionLayer ibsSelectionLayer) {
		try {
			if (ibsSelectionLayer != null) {
				logger.info(" Going to get IBSDetail");
				 return iIBSVisualisationService.getIBSDetail(ibsSelectionLayer);
			} else {
				throw new RestException(ForesightConstants.INVALID_PARAMETERS);
			}
		} catch (DaoException | RestException e) {
			logger.info("===1==");
			throw new RestException(e.getMessage());
		} catch (Exception e) {
			logger.info("========2===");
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);

		}
	}

	@POST
	@Path("getDeviceDetail")
	public Map getDeviceDetail(IBSSelectionLayer ibsSelectionLayer) {
		try {
			if (ibsSelectionLayer != null) {
				logger.info("Going to get IBSDeviceDetail ");
				return iIBSVisualisationService.getDeviceDetail(ibsSelectionLayer);
			}

			else {
				throw new RestException(ForesightConstants.INVALID_PARAMETERS);
			}
		} catch (DaoException | RestException e) {
			throw new RestException(e.getMessage());
		} catch (Exception e) {
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}

	
	@POST
	@Path("getDevicePositionDetails")
	public Map getDevicePositionDetails(IBSSelectionLayer ibsSelectionLayer) {
		try {
			if (ibsSelectionLayer != null) {
				logger.info("Going to get getDevicePositionDetails ");
				return iIBSVisualisationService.getDevicePositionDetails(ibsSelectionLayer);
			}

			else {
				throw new RestException(ForesightConstants.INVALID_PARAMETERS);
			}
		} catch (DaoException | RestException e) {
			throw new RestException(e.getMessage());
		} catch (Exception e) {
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}

}

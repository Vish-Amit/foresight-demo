package com.inn.foresight.module.nv.dashboard.service;

import java.util.List;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.module.nv.dashboard.model.NVDashboardMeta;

/** The Interface INVDashboardMetaService. */
public interface INVDashboardMetaService extends IGenericService<Integer, NVDashboardMeta> {

	/**
	 * Gets the all nv dashboard meta data.
	 *
	 * @return the all nv dashboard meta data
	 * @throws RestException the rest exception
	 */
	List<NVDashboardMeta> getAllNvDashboardMetaData();

	
}

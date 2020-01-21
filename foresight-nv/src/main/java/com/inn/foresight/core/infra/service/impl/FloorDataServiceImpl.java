package com.inn.foresight.core.infra.service.impl;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.infra.constants.InBuildingConstants;
import com.inn.foresight.core.infra.dao.IFloorDataDao;
import com.inn.foresight.core.infra.dao.IWingDataDao;
import com.inn.foresight.core.infra.model.Floor;
import com.inn.foresight.core.infra.model.Wing;
import com.inn.foresight.core.infra.service.IFloorDataService;
import com.inn.foresight.core.infra.service.IUnitDataService;
import com.inn.foresight.core.infra.utils.InBuildingUtils;
import com.inn.foresight.core.infra.wrapper.FloorWrapper;
import com.inn.foresight.core.infra.wrapper.WingWrapper;

/** The Class FloorDataServiceImpl. */
@Service("FloorDataServiceImpl")
public class FloorDataServiceImpl extends InBuildingConstants implements IFloorDataService {
	/** The logger. */
	private Logger logger = LogManager.getLogger(FloorDataServiceImpl.class);

	/** The i floor data dao. */
	@Autowired
	IFloorDataDao iFloorDataDao;

	/** The i wing data dao. */
	@Autowired
	IWingDataDao iWingDataDao;

	/** The i unit data service. */
	@Autowired
	IUnitDataService iUnitDataService;

	/**
	 * Creates the floor.
	 *
	 * @param floorData
	 *            the floor data
	 * @return the floor
	 * @throws RestException
	 *             the rest exception
	 */
	@Override
	public Floor createFloor(Floor floorData) {
		try {
			return iFloorDataDao.createFloor(floorData);
		} catch (DaoException e) {
			logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}

	/**
	 * Find by pk.
	 *
	 * @param id
	 *            the id
	 * @return the floor
	 * @throws RestException
	 *             the rest exception
	 */
	@Override
	public Floor findByPk(Integer id) {
		try {
			return iFloorDataDao.findByPk(id);
		} catch (DaoException e) {
			logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}

	/**
	 * Updates the floor.
	 *
	 * @param floorData
	 *            the floor data
	 * @return the floor
	 * @throws RestException
	 *             the rest exception
	 */

	@Override
	public Floor updateFloor(Floor floorData) {
		try {
			return iFloorDataDao.updateFloor(floorData);
		} catch (DaoException e) {
			logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}

	/**
	 * Delete floors.
	 *
	 * @param floorWrappers
	 *            the floor wrappers
	 */
	@Override
	public void deleteFloors(List<FloorWrapper> floorWrappers) {
		for (FloorWrapper floorWrapper : floorWrappers) {
			try {
				Floor floor = findByPk(floorWrapper.getFloorId());
				floor.setIsDeleted(true);
				updateFloor(floor);
				iUnitDataService.deleteUnits(floorWrapper.getUnitList());
			} catch (RestException e) {
				logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			}
		}
	}

	/**
	 * Creates the floor list.
	 *
	 * @param wingData
	 *            the wing data
	 * @param wingWrapper
	 *            the wing wrapper
	 * @throws RestException
	 *             the rest exception
	 */
	@Override
	public void createOrUpdateFloorList(Wing wingData, WingWrapper wingWrapper) {
		logger.info("Going to createFloorList");
		for (FloorWrapper floorWrapper : wingWrapper.getFloorList()) {
				Floor floorData = null;
				if (floorWrapper.getFloorId() != null) {
					floorData = updateFloor(wingData, floorWrapper);
				} else {
					floorData = createFloor(wingData, floorWrapper);
				}
				iUnitDataService.createOrUpdateUnitList(floorData, floorWrapper);
			} 
		wingWrapper.setCreationTime(wingData.getCreationTime().getTime());
		wingWrapper.setModificationTime(wingData.getModificationTime().getTime());
		wingWrapper.setWingId(wingData.getId());
	}

	/**
	 * Creates the floor.
	 *
	 * @param wingData the wing data
	 * @param floorWrapper the floor wrapper
	 * @return the floor
	 * @throws RestException the rest exception
	 */
	private Floor createFloor(Wing wingData, FloorWrapper floorWrapper) {
		Floor floorData = new Floor();
		floorData = InBuildingUtils.getFloorData(floorData, floorWrapper, wingData);
		floorData = createFloor(floorData);
		floorWrapper.setFloorId(floorData.getId());
		return floorData;
	}

	/**
	 * Update floor.
	 *
	 * @param wingData the wing data
	 * @param floorWrapper the floor wrapper
	 * @return the floor
	 * @throws RestException the rest exception
	 */
	private Floor updateFloor(Wing wingData, FloorWrapper floorWrapper) {
		Floor floorData = findByPk(floorWrapper.getFloorId());
		floorData = InBuildingUtils.getFloorData(floorData, floorWrapper, wingData);
		return updateFloor(floorData);
	}

	/**
	 * Update floor list.
	 *
	 * @param wingWrapper
	 *            the wing wrapper
	 * @throws RestException
	 *             the rest exception
	 */
	@Override
	public void updateFloorList(WingWrapper wingWrapper) {
		for (FloorWrapper floorWrapper : wingWrapper.getFloorList()) {
			try {
				Floor floor = findByPk(floorWrapper.getFloorId());
				updateFloor(InBuildingUtils.getFloorData(floor, floorWrapper, null));
				iUnitDataService.updateUnitList(floorWrapper);
			} catch (RestException e) {
				logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			}
		}
	}

	/**
	 * Gets the all floor wrapper for wing.
	 *
	 * @param wingId the wing id
	 * @return the all floor wrapper for wing
	 */
	@Override
	public List<FloorWrapper> getAllFloorWrapperForWing(Integer wingId) {
		List<FloorWrapper> floorWrapperList = null;
		try {
			floorWrapperList = iFloorDataDao.getAllFloorForWing(iWingDataDao.findByPk(wingId));

			floorWrapperList.parallelStream()
							.forEach(e -> e.setUnitList(iUnitDataService.getAllUnitWrapperForFloor(e.getFloorId())));

		} catch (DaoException e) {
			logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
		}
		return floorWrapperList;
	}

}

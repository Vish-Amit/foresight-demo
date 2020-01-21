package com.inn.foresight.core.infra.service.impl;

import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.constants.InBuildingConstants;
import com.inn.foresight.core.infra.dao.IFloorDataDao;
import com.inn.foresight.core.infra.dao.IUnitDataDao;
import com.inn.foresight.core.infra.model.Floor;
import com.inn.foresight.core.infra.model.Unit;
import com.inn.foresight.core.infra.service.IUnitDataService;
import com.inn.foresight.core.infra.utils.InBuildingUtils;
import com.inn.foresight.core.infra.wrapper.FloorWrapper;
import com.inn.foresight.core.infra.wrapper.UnitWrapper;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** The Class UnitDataServiceImpl. */
@Service("UnitDataServiceImpl")
public class UnitDataServiceImpl extends InBuildingConstants implements IUnitDataService {
	/** The logger. */
	private Logger logger = LogManager.getLogger(UnitDataServiceImpl.class);

	/** The i unit data dao. */
	@Autowired
	IUnitDataDao iUnitDataDao;

	/** The i floor data dao. */
	@Autowired
	IFloorDataDao iFloorDataDao;

	/**
	 * Creates the unit.
	 *
	 * @param unitData
	 *            the unit data
	 * @return the unit
	 * @throws RestException
	 *             the rest exception
	 */
	@Override
	public Unit createUnit(Unit unitData) {
		try {
			return iUnitDataDao.createUnit(unitData);
		} catch (DaoException e) {
			logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}

	/**
	 * Find by pk.
	 *
	 * @param id            the id
	 * @return the unit
	 */
	@Override
	public Unit findByPk(Integer id)  {
		Unit unit=null;
		try {
			unit= iUnitDataDao.findByPk(id);
		} catch (DaoException e) {
			logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
		}
		return unit;
	}

	/**
	 * Updates the unit.
	 *
	 * @param unitData
	 *            the unit data
	 * @return the unit
	 * @throws RestException
	 *             the rest exception
	 */
	@Override
	public Unit updateUnit(Unit unitData) {
		try {
			return iUnitDataDao.updateUnit(unitData);
		} catch (DaoException e) {
			logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}

	/**
	 * Delete units.
	 *
	 * @param unitList
	 *            the unit list
	 * @throws RestException
	 *             the rest exception
	 */
	@Override
	public void deleteUnits(List<UnitWrapper> unitList) {
		for (UnitWrapper unitWrapper : unitList) {
			try {
				Unit unit = findByPk(unitWrapper.getUnitId());
				unit.setIsDeleted(true);
				updateUnit(unit);
			} catch (RestException e) {
				logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			}
		}
	}

	/**
	 * Creates the unit list.
	 *
	 * @param floorData
	 *            the floor data
	 * @param floorWrapper
	 *            the floor wrapper
	 * @throws RestException
	 *             the rest exception
	 */
	@Override
	public void createOrUpdateUnitList(Floor floorData, FloorWrapper floorWrapper) {
		logger.info("Going to createUnitList");
		for (UnitWrapper unitWrapper : floorWrapper.getUnitList()) {
				if (unitWrapper.getUnitId() != null) {
					updateUnit(floorData, unitWrapper);
				} else {
					createUnit(floorData, unitWrapper);
				}
		}
		floorWrapper.setCreationTime(floorData.getCreationTime().getTime());
		floorWrapper.setModificationTime(floorData.getModificationTime().getTime());
		floorWrapper.setFloorId(floorData.getId());
	}

	/**
	 * Creates the unit.
	 *
	 * @param floorData the floor data
	 * @param unitWrapper the unit wrapper
	 * @throws RestException the rest exception
	 */
	private void createUnit(Floor floorData, UnitWrapper unitWrapper) {
		Unit unitData = new Unit();
		unitData = InBuildingUtils.getUnitData(unitData, unitWrapper, floorData);
		unitData = createUnit(unitData);
		unitWrapper.setUnitId(unitData.getId());
		unitWrapper.setCreationTime(unitData.getCreationTime().getTime());
		unitWrapper.setModificationTime(unitData.getModificationTime().getTime());
	}

	/**
	 * Update unit.
	 *
	 * @param floorData the floor data
	 * @param unitWrapper the unit wrapper
	 * @throws RestException the rest exception
	 */
	private void updateUnit(Floor floorData, UnitWrapper unitWrapper) {
		Unit unitData = findByPk(unitWrapper.getUnitId());
		unitData = InBuildingUtils.getUnitData(unitData, unitWrapper, floorData);
		updateUnit(unitData);
	}

	/**
	 * Update unit list.
	 *
	 * @param floorWrapper
	 *            the floor wrapper
	 * @throws RestException
	 *             the rest exception
	 */
	@Override
	public void updateUnitList(FloorWrapper floorWrapper) {
		for (UnitWrapper unitWrapper : floorWrapper.getUnitList()) {
			Unit unit = null;
			try {
				unit = findByPk(unitWrapper.getUnitId());
				updateUnit(InBuildingUtils.getUnitData(unit, unitWrapper, null));
			} catch (RestException e) {
				logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			}
		}
	}

	/**
	 * Gets the all unit wrapper for floor.
	 *
	 * @param floorId            the floor id
	 * @return the all unit wrapper for floor
	 */
	@Override public List<UnitWrapper> getAllUnitWrapperForFloor(Integer floorId) {
		try {
			Floor floor = iFloorDataDao.findByPk(floorId);
			List<UnitWrapper> unitList = iUnitDataDao.getAllUnitForFloor(floor);
			if (Utils.isValidList(unitList)) {
				Map<Integer, List<UnitWrapper>> unitWiseMap = unitList.stream()
																	  .filter(uw -> uw != null
																			  && uw.getUnitId() != null)
																	  .collect(Collectors.groupingBy(
																			  UnitWrapper::getUnitId,
																			  Collectors.toList()));
				return filterUnitWrapperAndReturnList(unitWiseMap);
			}
		} catch (DaoException e) {
			logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	private List<UnitWrapper> filterUnitWrapperAndReturnList(Map<Integer, List<UnitWrapper>> unitWiseMap) {
		List<UnitWrapper> filteredUnitList = new ArrayList<>();
		for (Map.Entry<Integer, List<UnitWrapper>> unitWiseEntry : unitWiseMap.entrySet()) {
			UnitWrapper approvedUnitWrapper = unitWiseEntry.getValue()
														   .stream()
														   .filter(uw -> uw.getIsFloorPlanApproved() != null
																   && uw.getIsFloorPlanApproved())
														   .findAny()
														   .orElse(null);
			if (approvedUnitWrapper == null) {
				approvedUnitWrapper = unitWiseEntry.getValue()
												   .stream()
												   .filter(uw -> uw.getModificationTime() != null)
												   .max(Comparator.comparing(UnitWrapper::getModificationTime))
												   .orElse(null);
			}
			
			filteredUnitList.add(approvedUnitWrapper);
		}
		filteredUnitList.sort(Comparator.comparingInt(UnitWrapper::getUnitId));
		return Utils.isValidList(filteredUnitList) ? filteredUnitList : null;
	}

}

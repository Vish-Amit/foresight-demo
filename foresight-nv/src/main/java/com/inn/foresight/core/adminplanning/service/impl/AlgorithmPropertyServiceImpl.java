package com.inn.foresight.core.adminplanning.service.impl;

import static j2html.TagCreator.b;
import static j2html.TagCreator.body;
import static j2html.TagCreator.br;
import static j2html.TagCreator.head;
import static j2html.TagCreator.html;
import static j2html.TagCreator.table;
import static j2html.TagCreator.td;
import static j2html.TagCreator.text;
import static j2html.TagCreator.th;
import static j2html.TagCreator.tr;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.envers.NotAudited;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.impl.AbstractService;
import com.inn.core.generic.utils.Preconditions;
import com.inn.foresight.core.adminplanning.dao.IAlgorithmDao;
import com.inn.foresight.core.adminplanning.dao.IAlgorithmPropertyDao;
import com.inn.foresight.core.adminplanning.model.Algorithm;
import com.inn.foresight.core.adminplanning.model.AlgorithmProperty;
import com.inn.foresight.core.adminplanning.service.IAlgorithmPropertyService;
import com.inn.foresight.core.adminplanning.util.AlgorithmPlanningConstant;
import com.inn.foresight.core.adminplanning.wrapper.AlgorithmResponse;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.product.um.geography.dao.GeographyL1Dao;
import com.inn.product.um.geography.dao.GeographyL2Dao;
import com.inn.product.um.geography.dao.GeographyL3Dao;
import com.inn.product.um.geography.dao.GeographyL4Dao;
import com.inn.product.um.user.model.User;
import com.inn.product.um.user.service.impl.UserContextServiceImpl;

import j2html.tags.ContainerTag;

/**
 * The Class AlgorithmPropertyServiceImpl.
 */
@Service("AlgorithmPropertyServiceImpl")

public class AlgorithmPropertyServiceImpl extends AbstractService<Integer, AlgorithmProperty>
		implements IAlgorithmPropertyService {

	/** The logger. */
	private Logger logger = LogManager.getLogger(AlgorithmPropertyServiceImpl.class);

	/** The i algorithm property dao. */
	@Autowired
	private IAlgorithmPropertyDao iAlgorithmPropertyDao;

	/** The algorithm dao. */
	@Autowired
	private IAlgorithmDao algorithmDao;

	
	/** The l 1 dao. */
	@Autowired
	private GeographyL1Dao l1Dao;

	/** The l 2 dao. */
	@Autowired
	private GeographyL2Dao l2Dao;

	/** The l 3 dao. */
	@Autowired
	private GeographyL3Dao l3Dao;

	/** The l 4 dao. */
	@Autowired
	private GeographyL4Dao l4Dao;

	/**
	 * Sets the dao.
	 *
	 * @param dao
	 *            the new dao
	 */
	@Autowired
	public void setDao(IAlgorithmPropertyDao dao) {
		super.setDao(dao);
		this.iAlgorithmPropertyDao = dao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.inn.foresight.core.adminplanning.service.IAlgorithmPropertyService#
	 * getExistingExceptions(java.lang.Integer)
	 */
	@Transactional
	@Override
	public List<AlgorithmResponse> getExistingExceptions(Integer algoId) {
		try {
			return iAlgorithmPropertyDao.getExistingExceptions(algoId);
		} catch (DaoException e) {
			logger.error("Exception in getExistingExceptions : {}", Utils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.inn.foresight.core.adminplanning.service.IAlgorithmPropertyService#
	 * getProperties(java.lang.Integer, java.lang.String, java.lang.Integer)
	 */
	@Transactional
	@Override
	public List<AlgorithmProperty> getProperties(Integer algorithmId, String type, Integer exceptionId) {
		logger.info("going to get properties for algoid : {} type : {}, exception : {}", algorithmId, type,
				exceptionId);
		try {
			if (exceptionId == null && type == null) { // If exception missing, then return PAN properties
				return iAlgorithmPropertyDao.getPANProperties(algorithmId);
			} else {
				return iAlgorithmPropertyDao.getPropertiesForException(algorithmId, exceptionId, type);
			}
		} catch (DaoException de) {
			logger.error("Exception in getting algorithm properties : {}", Utils.getStackTrace(de));
			throw new RestException(de.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.inn.foresight.core.adminplanning.service.IAlgorithmPropertyService#
	 * resetAlgorithmPropertyData(java.lang.Integer, java.lang.String,
	 * java.lang.Integer)
	 */
	@Transactional
	@Override
	public void resetAlgorithmPropertyData(Integer algoId, String type, Integer exceptionId, String reason) {
		try {
			Algorithm algorithm = algorithmDao.findByPk(algoId);
			User user = UserContextServiceImpl.getUserInContext();
			if (exceptionId != null) {// If no exception, then reset for PAN
				resetPropertyException(type, exceptionId, user, algoId);
			} else {
				resetPANProperties(algoId, user, reason);
			}
			if (algorithm.getMailEnabled() != null && algorithm.getMailEnabled()) {
				sendMail(null, user, AlgorithmPlanningConstant.RESET_STATUS, algorithm,
						getGeographyName(type, exceptionId));
			}
		} catch (Exception e) {
			logger.error("Exception occur in @resetAlgorithmPropertyData method error msg : {}",
					Utils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}

	/**
	 * Gets the geography name.
	 *
	 * @param type
	 *            the type
	 * @param exceptionId
	 *            the exception id
	 * @return the geography name
	 */
	private String getGeographyName(String type, Integer exceptionId) {
		try {
			switch (type) {
			case AlgorithmPlanningConstant.L1:
				return l1Dao.findByPk(exceptionId).getName();
			case AlgorithmPlanningConstant.L2:
				return l2Dao.findByPk(exceptionId).getName();
			case AlgorithmPlanningConstant.L3:
				return l3Dao.findByPk(exceptionId).getName();
			case AlgorithmPlanningConstant.L4:
				return l4Dao.findByPk(exceptionId).getName();
			default:
				return AlgorithmPlanningConstant.PAN;
			}
		} catch (DaoException e) {
			logger.error("Exception in getting geography name: {}", Utils.getStackTrace(e));
		} catch (NullPointerException e) {
			logger.error("Geography is not present for type :{} and exceptionId: {}", type, exceptionId);
		}
		return ForesightConstants.BLANK_STRING;
	}

	/**
	 * Reset property exception.
	 *
	 * @param type
	 *            the type
	 * @param exceptionId
	 *            the exception id
	 * @param user
	 *            the user
	 * @param algoId
	 *            the algo id
	 * @throws DaoException
	 *             the dao exception
	 */
	private void resetPropertyException(String type, Integer exceptionId, User user, Integer algoId) {
		List<AlgorithmProperty> resetAlgorithmPropertyData = iAlgorithmPropertyDao.getPropertiesForException(algoId,
				exceptionId, type);
		logger.info("Inside resetAlgorithmPropertyData method list size : {}", resetAlgorithmPropertyData.size());
		for (AlgorithmProperty algoTemp : resetAlgorithmPropertyData) {
			algoTemp.setDeleted(true);
			algoTemp.setModifier(user);
			algoTemp.setModificationTime(new Date());

			iAlgorithmPropertyDao.update(algoTemp);
		}
	}

	/**
	 * Reset PAN properties.
	 *
	 * @param id
	 *            the id
	 * @param user
	 *            the user
	 * @param reason
	 *            the reason
	 * @throws DaoException
	 *             the dao exception
	 */
	private void resetPANProperties(Integer id, User user, String reason) {
		List<AlgorithmProperty> resetPANProperties = iAlgorithmPropertyDao.getPANProperties(id);
		if (resetPANProperties != null) {
			for (AlgorithmProperty algorData : resetPANProperties) {
				if (!algorData.getDefaultValue().equalsIgnoreCase(algorData.getValue())) { // If same, reset doesn't
																							// have any effect
					algorData.setPreviousValue(algorData.getValue());
					algorData.setValue(algorData.getDefaultValue());
					algorData.setModifier(user);
					algorData.setModificationTime(new Date());
					algorData.setReason(reason);
					iAlgorithmPropertyDao.update(algorData);
				}
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.inn.foresight.core.adminplanning.service.IAlgorithmPropertyTempService#
	 * createException(java.lang.Integer, java.util.List)
	 */
	@Transactional
	@Override
	public void createProperties(Integer algorithmId, List<AlgorithmProperty> newExceptions) {
		try {
			User creator = UserContextServiceImpl.getUserInContext();
			Algorithm parent = getUpdatedParent(algorithmId, creator);
			createPropertiesForPAN(newExceptions, creator, parent);
			if (parent.getMailEnabled() != null && parent.getMailEnabled()) {
				proceedToSendMail(newExceptions, creator, parent);
			}
		} catch (RestException re) {
			throw re;
		} catch (Exception e) {
			logger.error("Exception in creating exception : {}", Utils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}

	/**
	 * Proceed to send mail.
	 *
	 * @param newExceptions
	 *            the new exceptions
	 * @param creator
	 *            the creator
	 * @param parent
	 *            the parent
	 */
	private void proceedToSendMail(List<AlgorithmProperty> newExceptions, User creator, Algorithm parent) {
		Set<String> geoList = new HashSet<>();
		ContainerTag tableTag = createTableTemplate(AlgorithmPlanningConstant.CREATE_NEW_EXCEPTION);
		try {
			for (AlgorithmProperty exception : newExceptions) {
				geoList.add(getGeographyName(exception));
				createTableRow(tableTag, exception.getConfiguration().getName(), exception.getDefaultValue(),
						exception.getValue());
			}
		} catch (Exception e) {
			logger.error("Exception in sending mail : {}", Utils.getStackTrace(e));
		}
		sendMail(tableTag, creator, AlgorithmPlanningConstant.CREATE_NEW_EXCEPTION, parent,
				convertListToString(geoList));
	}

	/**
	 * Convert list to string.
	 *
	 * @param list
	 *            the list
	 * @return the string
	 */
	private String convertListToString(Set<String> list) {
		StringBuilder bld = new StringBuilder();
		for (String str : list) {
			if (str != null && !str.isEmpty()) {
				if (bld.length() > 0) {
					bld.append(ForesightConstants.COMMA + str);
				} else {
					bld.append(str);
				}
			}
		}
		return bld.toString();
	}

	/**
	 * Gets the updated parent.
	 *
	 * @param algorithmId
	 *            the algorithm id
	 * @param creator
	 *            the creator
	 * @return the updated parent
	 * @throws DaoException
	 *             the dao exception
	 * @throws RestException
	 *             the rest exception
	 */
	private Algorithm getUpdatedParent(Integer algorithmId, User creator) {
		Algorithm parent = algorithmDao.findByPk(algorithmId);
		Preconditions.checkArgument(parent != null, AlgorithmPlanningConstant.INVALID_ALGORITHM);
		parent.setModifiedTime(new Date());
		parent.setModifier(creator);
		return parent;
	}

	/**
	 * Sets the property parameters.
	 *
	 * @param parent
	 *            the parent
	 * @param creator
	 *            the creator
	 * @param exception
	 *            the exception
	 */
	private void setPropertyParameters(Algorithm parent, User creator, AlgorithmProperty exception) {
		exception.setDeleted(false);
		exception.setModifier(creator);
		exception.setAlgorithm(parent);
		exception.setCreationTime(new Date());
		exception.setModificationTime(new Date());
		if (exception.getConfiguration() != null) {
			exception.setUiConfiguration(new Gson().toJson(exception.getConfiguration()));
		}
	}

	/**
	 * Gets the geography name.
	 *
	 * @param algorithmProperty
	 *            the algorithm property
	 * @return the geography name
	 */
	private String getGeographyName(AlgorithmProperty algorithmProperty) {
		try {
			if ((algorithmProperty.getGeographyL1Fk()) != null) {
				return algorithmProperty.getGeographyL1Fk().getName();
			} else if (algorithmProperty.getGeographyL2Fk() != null) {
				return algorithmProperty.getGeographyL2Fk().getName();
			} else if (algorithmProperty.getGeographyL3Fk() != null) {
				return algorithmProperty.getGeographyL3Fk().getName();
			} else if (algorithmProperty.getGeographyL4Fk() != null) {
				return algorithmProperty.getGeographyL4Fk().getName();
			} else {
				return AlgorithmPlanningConstant.PAN;
			}
		} catch (NullPointerException e) {
			logger.error("Geography is not found : {}", Utils.getStackTrace(e));
		}
		return ForesightConstants.BLANK_STRING;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.inn.foresight.core.adminplanning.service.IAlgorithmPropertyService#
	 * updateProperties(java.util.List, java.lang.Integer)
	 */
	@Transactional
	@Override
	public void updateProperties(List<AlgorithmProperty> propertyList, Integer id) {
		try {
			ContainerTag tableTag = createTableTemplate(AlgorithmPlanningConstant.UPDATE_STATUS);
			Algorithm algorithm = algorithmDao.findByPk(id);
			User user = UserContextServiceImpl.getUserInContext();
			for (AlgorithmProperty updateProperty : propertyList) {
				AlgorithmProperty property = iAlgorithmPropertyDao.findByPk(updateProperty.getId());
				property.setModificationTime(new Date());
				property.setModifier(user);
				copyProperty(updateProperty, property);
				property = iAlgorithmPropertyDao.update(property);
				logger.info("is mail enabled:- {}", algorithm.getMailEnabled());
				if (algorithm.getMailEnabled() != null && algorithm.getMailEnabled()) {
					createTableRow(tableTag, property.getConfiguration().getName(), property.getPreviousValue(),
							property.getValue());
				}
			}
			if (algorithm.getMailEnabled() != null && algorithm.getMailEnabled()) {
				logger.info("inside  mail @updateProperties");
				sendMail(tableTag, user, AlgorithmPlanningConstant.UPDATE_STATUS, algorithm,
						getGeographyName(propertyList.get(0)));
			}
			algorithm.setModifiedTime(new Date());
			algorithmDao.update(algorithm);
		} catch (Exception e) {
			logger.error("Exception in updating property list : {}", Utils.getStackTrace(e));
		}
	}

	/**
	 * Copy property.
	 *
	 * @param updateProperty
	 *            the update property
	 * @param property
	 *            the property
	 */
	private void copyProperty(AlgorithmProperty updateProperty, AlgorithmProperty property) {
		if (updateProperty.getPreviousValue() != null) {
			property.setPreviousValue(updateProperty.getPreviousValue());
		}
		if (updateProperty.getValue() != null) {
			property.setValue(updateProperty.getValue());
		}
		if (updateProperty.getReason() != null) {
			property.setReason(updateProperty.getReason());
		}
		if (updateProperty.getName() != null) {
			property.setName(updateProperty.getName());
		}
		if (updateProperty.getDefaultValue() != null) {
			property.setDefaultValue(updateProperty.getDefaultValue());
		}
		if (updateProperty.getDescription() != null) {
			property.setDescription(updateProperty.getDescription());
		}
		if (updateProperty.getConfiguration() != null) {
			property.setUiConfiguration(new Gson().toJson(updateProperty.getConfiguration()));
		}
	}

	/**
	 * Creates the table row.
	 *
	 * @param createTableTemplate
	 *            the create table template
	 * @param name
	 *            the name
	 * @param value1
	 *            the value 1
	 * @param value2
	 *            the value 2
	 */
	private void createTableRow(ContainerTag createTableTemplate, String name, String value1, String value2) {
		createTableTemplate.with(tr(td(text(name)), td(text(value1)), td(text(value2))));
	}

	/**
	 * Send mail.
	 *
	 * @param tableTemplate
	 *            the table template
	 * @param modifier
	 *            the modifier
	 * @param action
	 *            the action
	 * @param algorithm
	 *            the algorithm
	 * @param geography
	 *            the geography
	 */
	public void sendMail(ContainerTag tableTemplate, User modifier, String action, Algorithm algorithm,
			String geography) {
		try {
			String message = createMailContent(tableTemplate, modifier, action, algorithm.getDisplayName(), geography);
			logger.info("mail message : {}", message);
			Utils.createEmailNotification(algorithm.getRecipients(), null, null,
					AlgorithmPlanningConstant.MAIL_NOTIFICATION_SUBJECT, message, null, null);
		} catch (Exception e) {
			logger.error("Could not send Algorithm Property Mail : {}", ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Creates the table template.
	 *
	 * @param action
	 *            the action
	 * @return the container tag
	 */
	private ContainerTag createTableTemplate(String action) {
		ContainerTag table = table().attr("border", 1);
		if (action.equalsIgnoreCase(AlgorithmPlanningConstant.UPDATE_STATUS)) {
			return table.with(tr(th(text("Parameter Name")), th(text("Previous Value")), th(text("Modified Value"))));
		} else {
			return table.with(tr(th(text("Parameter Name")), th(text("Default Value")), th(text("Created Value"))));
		}
	}

	/**
	 * Creates the mail content.
	 *
	 * @param createTableTemplate
	 *            the create table template
	 * @param modifier
	 *            the modifier
	 * @param action
	 *            the action
	 * @param algorithmName
	 *            the algorithm name
	 * @param geography
	 *            the geography
	 * @return the string
	 */
	private String createMailContent(ContainerTag createTableTemplate, User modifier, String action,
			String algorithmName, String geography) {
		try {
			String date = DateFormatUtils.format(new Date(), AlgorithmPlanningConstant.MAIL_DATE_FORMAT);
			ContainerTag body = body(text("Dear Administrator, "), br(), br());
			body.withText("User " + getModifier(modifier) + " has " + action + " in the ");
			body.with(b("\"Administration>>Algorithms>>Algorithm Planning\" "), text("section."), br(), br());
			body.with(text("Algorithm Name: "), text(algorithmName), br(), br());
			body.with(text("Date & Time: "), text(date), br(), br());
			body.with(text("Geography: " + geography));
			body.with(createTableTemplate);
			body.with(br(), br(), text("Regards,"), br(), text("Foresight Team"));
			return html(head(), body).render();
		} catch (Exception e) {
			logger.error("exception in creationg mail content: {}", Utils.getStackTrace(e));
		}
		return null;
	}

	/**
	 * Gets the modifier.
	 *
	 * @param modifier
	 *            the modifier
	 * @return the modifier
	 */
	private String getModifier(User modifier) {
		if (modifier != null)
			return modifier.getEmail();
		else
			return ForesightConstants.BLANK_STRING;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.inn.foresight.core.adminplanning.service.IAlgorithmPropertyService#
	 * getPropertyByName(java.lang.String)
	 */
	@Override
	public List<AlgorithmProperty> getPropertyByName(String propertyName) {
		try {
			return iAlgorithmPropertyDao.getPropertyByName(propertyName);
		} catch (DaoException e) {
			logger.error("Exception inside getPropertyByName {}", Utils.getStackTrace(e));
			throw new RestException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.inn.foresight.core.adminplanning.service.IAlgorithmPropertyService#
	 * deleteProperty(java.lang.Integer)
	 */
	@Transactional
	@Override
	public void deleteProperty(Integer propertyId) {
		try {
			AlgorithmProperty existingProperty = iAlgorithmPropertyDao.findByPk(propertyId);
			Preconditions.checkNotNull(existingProperty, AlgorithmPlanningConstant.PROPERTY_NOT_FOUND);
			Preconditions.checkNotNull(existingProperty.getAlgorithmId(), AlgorithmPlanningConstant.PROPERTY_NOT_FOUND);
			List<AlgorithmProperty> childPropertyList = iAlgorithmPropertyDao
					.getPropertyByNameAndId(existingProperty.getAlgorithmId(), existingProperty.getName());
			for (AlgorithmProperty property : childPropertyList) {
				property.setDeleted(true);
				property.setModificationTime(new Date());
				property.setModifier(UserContextServiceImpl.getUserInContext());
				iAlgorithmPropertyDao.update(property);
			}
		} catch (DaoException dao) {
			logger.error("Exception in transaction : {}", Utils.getStackTrace(dao));
			throw new RestException(dao);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.inn.foresight.core.adminplanning.service.IAlgorithmPropertyService#
	 * getPropertyHistory(java.lang.Integer)
	 */
	@Transactional
	@Override
	public List<Object[]> getPropertyHistory(Integer propertyId) {
		try {
			return iAlgorithmPropertyDao.getPropertyHistory(propertyId);
		} catch (DaoException de) {
			throw new RestException(de);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.inn.foresight.core.adminplanning.service.IAlgorithmPropertyService#
	 * addProperties(java.lang.Integer, java.util.List)
	 */
	@Transactional
	@Override
	public void addProperties(Integer algorithmId, List<AlgorithmProperty> properties) {
		try {
			User creator = UserContextServiceImpl.getUserInContext();
			Algorithm parent = getUpdatedParent(algorithmId, creator);
			createPropertiesForPAN(properties, creator, parent); // This method will set property parameters in
																	// properties
			createPropertiesForExistingExceptions(properties, algorithmId);
		} catch (Exception e) {
			logger.error("Exception in adding properties : {}", Utils.getStackTrace(e));
			throw new RestException(e);
		}
	}

	/**
	 * Creates the properties for existing exceptions.
	 *
	 * @param properties
	 *            the properties
	 * @param algorithmId
	 *            the algorithm id
	 * @throws RestException
	 *             the rest exception
	 * @throws DaoException
	 *             the dao exception
	 */
	private void createPropertiesForExistingExceptions(List<AlgorithmProperty> properties, Integer algorithmId) {
		List<AlgorithmResponse> exceptions = getExistingExceptions(algorithmId);
		for (AlgorithmResponse exception : exceptions) {
			for (AlgorithmProperty property : properties) {
				AlgorithmProperty propertyClone = (AlgorithmProperty) SerializationUtils.clone(property);
				switch (exception.getType()) {
				case AlgorithmPlanningConstant.L1:
					propertyClone.setGeographyL1Fk(l1Dao.findByPk(exception.getId()));
					break;
				case AlgorithmPlanningConstant.L2:
					propertyClone.setGeographyL2Fk(l2Dao.findByPk(exception.getId()));
					break;
				case AlgorithmPlanningConstant.L3:
					propertyClone.setGeographyL3Fk(l3Dao.findByPk(exception.getId()));
					break;
				case AlgorithmPlanningConstant.L4:
					propertyClone.setGeographyL4Fk(l4Dao.findByPk(exception.getId()));
					break;
				default:
				}
				iAlgorithmPropertyDao.create(propertyClone);
			}
		}
	}

	/**
	 * Creates the properties for PAN.
	 *
	 * @param properties
	 *            the properties
	 * @param creator
	 *            the creator
	 * @param parent
	 *            the parent
	 * @throws DaoException
	 *             the dao exception
	 */
	private void createPropertiesForPAN(List<AlgorithmProperty> properties, User creator, Algorithm parent) {
		for (AlgorithmProperty property : properties) {
			setPropertyParameters(parent, creator, property);
			iAlgorithmPropertyDao.create(property);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.inn.foresight.core.adminplanning.service.IAlgorithmPropertyService#
	 * updatePropertiesConfiguration(java.util.List, java.lang.Integer)
	 */
	@Transactional
	@Override
	@NotAudited
	public void updatePropertiesConfiguration(List<AlgorithmProperty> propertyList, Integer algorithmId) {
		try {
			User user =UserContextServiceImpl.getUserInContext();
			logger.info("user name : {} and  algoListSize : {}", user, propertyList.size());
			for (AlgorithmProperty algorithmProperty : propertyList) {
				// name of property might be changed
				AlgorithmProperty property = iAlgorithmPropertyDao.findByPk(algorithmProperty.getId());
				List<AlgorithmProperty> childPropertyList = iAlgorithmPropertyDao.getPropertyByNameAndId(algorithmId,
						property.getName());
				logger.info("PropertyList: {}", childPropertyList);
				for (AlgorithmProperty childProperty : childPropertyList) {
					if (isParameterChange(algorithmProperty, childProperty)) {
						logger.info("inside @isParameterChange");
						childProperty.setModificationTime(new Date());
						childProperty.setModifier(user);
						updateParameters(algorithmProperty, childProperty);
						iAlgorithmPropertyDao.update(childProperty);
					}
				}
			}
		} catch (DaoException e) {
			logger.error("Exception in transaction : {}", Utils.getStackTrace(e));
			throw new RestException(e);
		}
	}


	
	
	private boolean isParameterChange(AlgorithmProperty algorithmProperty, AlgorithmProperty childProperty) {
		if (algorithmProperty.getName() != null
				&& !childProperty.getName().equalsIgnoreCase(algorithmProperty.getName())) {
			return true;
		}
		if (algorithmProperty.getDescription() != null
				&& !childProperty.getDescription().equalsIgnoreCase(algorithmProperty.getDescription())) {
			return true;
		}
		if (algorithmProperty.getDefaultValue() != null
				&& !childProperty.getDefaultValue().equalsIgnoreCase(algorithmProperty.getDefaultValue())) {
			return true;
		}
		if (algorithmProperty.getConfiguration() != null && !new Gson().toJson(algorithmProperty.getConfiguration())
				.equalsIgnoreCase(childProperty.getUiConfiguration())) {
			return true;
		}
		return false;
	}

	/**
	 * Update parameters.
	 *
	 * @param algorithmProperty
	 *            the algorithm property
	 * @param property
	 *            the property
	 */
	private void updateParameters(AlgorithmProperty algorithmProperty, AlgorithmProperty property) {
		if (algorithmProperty.getConfiguration() != null) {
			property.setUiConfiguration(new Gson().toJson(algorithmProperty.getConfiguration()));
		}
		if (algorithmProperty.getName() != null) {
			property.setName(algorithmProperty.getName());
		}
		if (algorithmProperty.getDefaultValue() != null) {
			property.setDefaultValue(algorithmProperty.getDefaultValue());
		}
		if (algorithmProperty.getDescription() != null) {
			property.setDescription(algorithmProperty.getDescription());
		}
	}

}

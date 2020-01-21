package com.inn.foresight.core.infra.dao.impl;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.QueryTimeoutException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.inn.commons.configuration.ConfigUtils;
import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.utils.Base64;
import com.inn.core.generic.utils.ExceptionUtil;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.dao.IAdvanceSearchDao;
import com.inn.foresight.core.infra.model.AdvanceSearch;
import com.inn.foresight.core.infra.model.AdvanceSearchConfiguration;
import com.inn.product.um.user.utils.UmConstants;

/** The Class AdvanceSearchDaoImpl. */
@Repository("AdvanceSearchDaoImpl")
public class AdvanceSearchDaoImpl extends HibernateGenericDao<Integer, AdvanceSearch> implements IAdvanceSearchDao {
	/** The logger. */
	private Logger logger = LogManager.getLogger(AdvanceSearchDaoImpl.class);

	/** The key. */
	private static byte[] key;

	/** Instantiates a new advance search dao impl. */
	public AdvanceSearchDaoImpl() {
		super(AdvanceSearch.class);
	}

	/**
	 * Creates the.
	 *
	 * @param anEntity the an entity
	 * @return the advance search
	 * @throws DaoException the dao exception
	 */
	@Override
	public AdvanceSearch create(AdvanceSearch anEntity) {
		return super.create(anEntity);
	}

	/**
	 * Update.
	 *
	 * @param anEntity the an entity
	 * @return the advance search
	 * @throws DaoException the dao exception
	 */
	@Override
	public AdvanceSearch update(AdvanceSearch anEntity) {
		logger.info("Updating record by an entity :  {} ", anEntity);
		return super.update(anEntity);
	}

	/**
	 * Delete.
	 *
	 * @param anEntity the an entity
	 * @throws DaoException the dao exception
	 */
	@Override
	public void delete(AdvanceSearch anEntity) {
		logger.info("Deleting record by an entity: {} ", anEntity);
		super.delete(anEntity);
	}

	/**
	 * Delete by pk.
	 *
	 * @param entityPk the entity pk
	 * @throws DaoException the dao exception
	 */
	@Override
	public void deleteByPk(Integer entityPk) {
		logger.info("Deleting record by id: {} ", entityPk);
		super.deleteByPk(entityPk);
	}

	/**
	 * Find by pk.
	 *
	 * @param entityPk the entity pk
	 * @return the advance search
	 * @throws DaoException the dao exception
	 */
	@Override
	public AdvanceSearch findByPk(Integer entityPk) {
		logger.info("Finding record by id: {} ", entityPk);
		return super.findByPk(entityPk);
	}

	/**
	 * Find all.
	 *
	 * @return the list
	 * @throws DaoException the dao exception
	 */
	@Override
	public List<AdvanceSearch> findAll() {
		return super.findAll();
	}

	/**
	 * Creates the in new transaction.
	 *
	 * @param entity the entity
	 * @return the advance search
	 * @throws DaoException the dao exception
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public AdvanceSearch createInNewTransaction(AdvanceSearch entity) {
		try {
			logger.info("Creating record by an entity in new transaction: {} ", entity);
			return create(entity);
		} catch (Exception e) {
			throw new DaoException(ExceptionUtil.generateExceptionCode("3", " Advance Search {} ", e));
		}
	}

	/**
	 * Gets the advance search by name.
	 *
	 * @param name the name
	 * @return the advance search by name
	 * @throws DaoException the dao exception
	 */
	@Override
	public List<AdvanceSearch> getAdvanceSearchByName(String name) {
		logger.info("getting AdvanceSearch by name {} ", name);
		List<AdvanceSearch> advanceSearchList = null;
		try {
			Query query = getEntityManager().createNamedQuery("getAdvanceSearchByName").setParameter("name", name);
			query.setMaxResults(10);
			return query.getResultList();
		} catch (Exception e) {
			throw new DaoException(ExceptionUtil.generateExceptionCode("3", "AdvanceSearch", e));
		}
	}

	/**
	 * Gets the advance search by type list.
	 *
	 * @param name     the name
	 * @param typeList the type list
	 * @return the advance search by type list
	 * @throws DaoException the dao exception
	 */
	@Override
	public List<AdvanceSearch> getAdvanceSearchByTypeList(String name, List<String> typeList, String levelType, List<Integer> geoId) {
		List<AdvanceSearch> advanceSearch = new ArrayList<>();
		logger.info("Inside getAdvanceSearchByTypeList");
		logger.info("name {} , typeList {}, levelType {}, geoId {} ", name, typeList, levelType, geoId);
		try {
			CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<AdvanceSearch> criteriaQuery = criteriaBuilder.createQuery(AdvanceSearch.class);
			Root<AdvanceSearch> root = criteriaQuery.from(AdvanceSearch.class);
			List<Predicate> predicates = new ArrayList<>();
			if (name != null) {
				predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.<String>get(ForesightConstants.DISPLAY_NAME), ForesightConstants.PERCENT + name + ForesightConstants.PERCENT)));
				predicates.add(root.<String>get(ForesightConstants.PRIORITYVALUE).isNotNull());
			}
			if (typeList != null && !typeList.isEmpty()) {
				predicates.add(root.get("advanceSearchConfiguration").get(ForesightConstants.TYPE).in(typeList));
			}
			if (levelType != null && geoId != null) {
				addPredicateForGeography(root, predicates, levelType, geoId);
			}
			criteriaQuery.where(predicates.toArray(new Predicate[] {}));
			Order asc1 = criteriaBuilder.asc(root.<String>get(ForesightConstants.PRIORITYVALUE));
			Order asc2 = criteriaBuilder.asc(criteriaBuilder.length(root.<String>get(ForesightConstants.NAME)));
			Order asc3 = criteriaBuilder.asc(root.<String>get(ForesightConstants.NAME));
			criteriaQuery.orderBy(new Order[] {asc1,asc2,asc3});
			Query query = getEntityManager().createQuery(criteriaQuery);
			query.setMaxResults(10);
			advanceSearch = query.getResultList();
			logger.info("advanceSearch list  :{} ", advanceSearch);

			return advanceSearch;
		} catch (Exception e) {
			logger.error("Error while getting advance search by type list: {} ", Utils.getStackTrace(e));
			throw new DaoException(ExceptionUtil.generateExceptionCode("3", "AdvanceSearch", e));
		}
	}

	private void addPredicateForGeography(Root<AdvanceSearch> advanceSearch, List<Predicate> predicates, String levelType, List<Integer> geoId) {
		logger.info("Inside addPredicateForGeography");
		try {
			if (UmConstants.L1.equals(levelType)) {
				predicates.add(advanceSearch.get("geographyL1").get(ForesightConstants.ID).in(geoId));
			} else if (UmConstants.L2.equals(levelType)) {
				predicates.add(advanceSearch.get("geographyL2").get(ForesightConstants.ID).in(geoId));
			} else if (UmConstants.L3.equals(levelType)) {
				predicates.add(advanceSearch.get("geographyL3").get(ForesightConstants.ID).in(geoId));
			} else if (UmConstants.L4.equals(levelType)) {
				predicates.add(advanceSearch.get("geographyL4").get(ForesightConstants.ID).in(geoId));
			} else if (UmConstants.SALES_L1.equals(levelType)) {
				predicates.add(advanceSearch.get("salesL1").get(ForesightConstants.ID).in(geoId));
			} else if (UmConstants.SALES_L2.equals(levelType)) {
				predicates.add(advanceSearch.get("salesL2").get(ForesightConstants.ID).in(geoId));
			} else if (UmConstants.SALES_L3.equals(levelType)) {
				predicates.add(advanceSearch.get("salesL3").get(ForesightConstants.ID).in(geoId));
			} else if (UmConstants.SALES_L4.equals(levelType)) {
				predicates.add(advanceSearch.get("salesL4").get(ForesightConstants.ID).in(geoId));
			}
		} catch (Exception e) {
			logger.error("Error while add predicate for geography : {} ", Utils.getStackTrace(e));
		}
	}

	@Override
	public List<String> getDistinctTypeList() {
		logger.info("Inside getDistinctTypeList");
		List<String> list = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery("getDistinctType");
			list = query.getResultList();
			if (list != null) {
				logger.info("list size: {} ", list.size());
			}
		} catch (Exception e) {
			logger.error("Error while getting distinct types {} ", Utils.getStackTrace(e));
		}
		return list;
	}

	/**
	 * Sign request.
	 *
	 * @param path  the path
	 * @param query the query
	 * @return the string
	 * @throws NoSuchAlgorithmException     the no such algorithm exception
	 * @throws InvalidKeyException          the invalid key exception
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 * @throws URISyntaxException           the URI syntax exception
	 */
	public String signRequest(String path, String query) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, URISyntaxException {
		String resource = path + '?' + query;
		SecretKeySpec sha1Key = new SecretKeySpec(key, "HmacSHA1");
		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(sha1Key);
		byte[] sigBytes = mac.doFinal(resource.getBytes());
		String signature = Base64.encodeToString(sigBytes, 0);
		signature = signature.replace('+', '-');
		signature = signature.replace('/', '_');
		return resource + "&signature=" + signature;
	}

	/**
	 * Gets the google response.
	 *
	 * @param param the param
	 * @return the google response
	 */
	@Override
	public Map getGoogleResponse(String param) {
		logger.info("Inside getGoogleResponse.");
		param = param.replace(" ", "%20");
		URL url;
		String finalURL = "";
		try {
			url = new URL(ConfigUtils.getString(ForesightConstants.GOOGLE_URL_PREFIX) + param + ConfigUtils.getString(ForesightConstants.GOOGLE_URL_SUFFIX));

			String keyString = ConfigUtils.getString(ForesightConstants.GOOGLE_KEY_STRING);
			keyString = keyString.replace('-', '+');
			keyString = keyString.replace('_', '/');
			String keyString1 = keyString.replace('"', ' ');
			this.key = Base64.decode(keyString1.trim(), 0);
			String request = signRequest(url.getPath(), url.getQuery());
			finalURL = url.getProtocol() + "://" + url.getHost() + request;
			logger.info("finalurl : {}", finalURL);
		} catch (MalformedURLException | InvalidKeyException | NoSuchAlgorithmException | UnsupportedEncodingException | URISyntaxException e) {
			logger.info("Exception occured during generation of url");
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		Map map = new HashMap();
		map.put("url", finalURL);
		logger.info("map  : {} ", map);
		return map;
	}

	@Override
	public AdvanceSearch getAdvanceSearchByTypeReference(String name) {
		AdvanceSearch result = null;
		try {
			Query query = getEntityManager().createNamedQuery("getAdvanceSearchByTypeReference").setParameter("name", name);
			result = (AdvanceSearch) query.getSingleResult();
		} catch (Exception e) {
			logger.error("Exception while getAdvanceSearchByTypeReference : {} ", ExceptionUtils.getStackTrace(e));
		}
		return result;
	}

	@Override
	public void deleteGalleryById(List<Integer> galleryId) {
		try {
			logger.info("going to Delete {} of ids from advanceSearch", galleryId);
			CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
			CriteriaDelete<AdvanceSearch> query = criteriaBuilder.createCriteriaDelete(AdvanceSearch.class);
			Root<AdvanceSearch> root = query.from(AdvanceSearch.class);
			query.where(root.get("typereference").in(galleryId));
			Query query1 = getEntityManager().createQuery(query);
			query1.executeUpdate();
			logger.info("succesfully Deleted following list {} of ids from advanceSearch", galleryId);
		} catch (NoResultException noResultException) {
			logger.error("No results were found");
		} catch (QueryTimeoutException queryTimeoutException) {
			logger.error("Query was enable to execute in specified time");
		} catch (Exception e) {
			logger.error("Error while updating Gallery and Smile status by id {} ", ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public List<AdvanceSearch> getAdvanceSearchByTypeListAndVendorType(String name, List<String> typeList,
			String levelType, List<Integer> geoId, List<String> vendorType,Map <String,List<String>> map) throws DaoException {
		List<AdvanceSearch> advanceSearch = new ArrayList<>();
		logger.info("Inside @Class : " + this.getClass().getName());
		logger.info("name {} ", name);
		logger.info("typeList {}", typeList);
		logger.info("Leveltype {}", levelType);
		logger.info("geoId {} ,vendorType{} ", geoId, vendorType);
		try {
			CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<AdvanceSearch> criteriaQuery = criteriaBuilder.createQuery(AdvanceSearch.class);
			Root<AdvanceSearch> root = criteriaQuery.from(AdvanceSearch.class);
			List<Predicate> predicates = new ArrayList<>();
			if (name != null) {
				predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.<String>get(ForesightConstants.NAME),
						ForesightConstants.PERCENT + name + ForesightConstants.PERCENT)));
			}
			if (vendorType != null) {
				predicates.add(criteriaBuilder.or(root.get("vendor").in(vendorType), root.get("vendor").isNull()));
			}
			if (typeList != null && !typeList.isEmpty()) {
				predicates.add(root.get("advanceSearchConfiguration").get(ForesightConstants.TYPE).in(typeList));
			}
			if (map.get("domain") != null && !map.get("domain").isEmpty()) {
				predicates.add(criteriaBuilder.or(root.get("domain").in(map.get("domain")), root.get("domain").isNull()));
			}
			if (levelType != null && geoId != null)
				addPredicateForGeography(root, predicates, levelType, geoId);

			criteriaQuery.where(predicates.toArray(new Predicate[] {}));
			criteriaQuery.orderBy(criteriaBuilder.asc(root.<String>get(ForesightConstants.PRIORITYVALUE)),
					criteriaBuilder.asc(root.<String>get(ForesightConstants.NAME)));
			Query query = getEntityManager().createQuery(criteriaQuery);
			query.setMaxResults(10);
			advanceSearch = query.getResultList();
			logger.info("advanceSearch list  :{} ", advanceSearch);

			return advanceSearch;
		} catch (Exception e) {
			logger.error("Error while getting advance search by type list: {} ", Utils.getStackTrace(e));
			throw new DaoException(ExceptionUtil.generateExceptionCode("3", "AdvanceSearch", e));
		}
	}

	@Override
	public AdvanceSearch getAdvanceSearchBySearchFieldNameAndTypeList(String name, List<String> type) {
		logger.info("Inside getAdvanceSearchBySearchFieldNameAndTypeList method");
		logger.info("name {} , typeList {}", name, type);
		AdvanceSearch advSearch = null;
		try {
			CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<AdvanceSearch> criteriaQuery = criteriaBuilder.createQuery(AdvanceSearch.class);
			Root<AdvanceSearch> root = criteriaQuery.from(AdvanceSearch.class);
			List<Predicate> predicateList = new ArrayList<>();
			if (name != null && type != null) {
				predicateList.add(criteriaBuilder.equal(root.get("name"), name));
				predicateList.add(root.get("advanceSearchConfiguration").get(ForesightConstants.TYPE).in(type));
			} else {
				logger.info("Invalid Parameters are Getting name :{},type :{}", name, type);
			}
			criteriaQuery.where(predicateList.toArray(new Predicate[] {}));
			TypedQuery<AdvanceSearch> resultList = getEntityManager().createQuery(criteriaQuery);
			advSearch = resultList.getSingleResult();
		} catch (NoResultException nre) {
			logger.error("Error while getting advance search by name and type  {} ", nre.getMessage());
		} catch (Exception e) {
			logger.error("Error while getting advance search by name and type  {} ", e.getMessage());
			throw new DaoException("Exception Occurred While Getting getAdvanceSearchBySearchFieldNameAndTypeList");
		}
		return advSearch;
	}
	
	@Override
	public AdvanceSearch getAdvanceSearchConfigurationByTypeAndTypeReference(String type, Integer typeReference) {
		logger.info("type :{},typeReference:{}", type, typeReference);

		AdvanceSearch advanceSearch = null;
		try {
			TypedQuery<AdvanceSearch> query = getEntityManager()
					.createNamedQuery("getAdvanceSearchConfigurationByTypeAndTypeReference", AdvanceSearch.class);
			query.setParameter("type", type);
			query.setParameter("typereference", typeReference);
			advanceSearch = query.getSingleResult();
			logger.info("advanceSearch :{}",advanceSearch);
		} catch (NoResultException nre) {
			logger.error("Error while getting advance search by type and typeReference  {} ", nre.getMessage());
		} catch (Exception e) {
			logger.error("Error while getting advance search by type and typeReference  {} ", e.getMessage());
			throw new DaoException("Exception Occurred While Getting getAdvanceSearchConfigurationByTypeAndTypeReference");
		}
		return advanceSearch;
	}
}

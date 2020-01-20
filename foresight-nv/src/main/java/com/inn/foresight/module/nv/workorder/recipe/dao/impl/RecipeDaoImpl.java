package com.inn.foresight.module.nv.workorder.recipe.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;
import com.inn.foresight.module.nv.workorder.recipe.dao.IRecipeDao;
import com.inn.foresight.module.nv.workorder.recipe.model.Recipe;
import com.inn.foresight.module.nv.workorder.recipe.wrapper.RecipeWrapper;

/** The Class RecipeDaoImpl. */
@Repository("RecipeDaoImpl")
public class RecipeDaoImpl extends HibernateGenericDao<Integer, Recipe> implements IRecipeDao {

	/** The logger. */
	private Logger logger = LogManager.getLogger(RecipeDaoImpl.class);

	/** Instantiates a new recipe dao impl. */
	public RecipeDaoImpl() {
		super(Recipe.class);
	}

	/**
	 * Gets List of All Recipe From Db.
	 *
	 * @return List of Recipe Id String
	 * @throws RestException the rest exception
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<RecipeWrapper> findAllRecipes(Integer lLimit, Integer uLimit) {
		logger.info("Going to get All Recipes List");

		Query query = getEntityManager().createNamedQuery("findAllRecipe");
		
		query.setFirstResult(lLimit);
		query.setMaxResults(uLimit - lLimit + 1);
		try {
			List<RecipeWrapper> list = query.getResultList();
			logger.info(NVWorkorderConstant.RESULT_SIZE_LOGGER, list.size());
			return list;
		} catch (Exception e) {
			logger.error(NVWorkorderConstant.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new DaoException(e.getMessage());
		}
	}

	/**
	 * Gets List of Recipe Ids LIKE provided recipeId From Db.
	 *
	 * @param recipeId the recipe id
	 * @return List of Recipe Id String
	 * @throws RestException the rest exception
	 * @Example RC-CSTM-1,RC-CSTM-2,RC-CSTM-3
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getRecipeIdList(String recipeId) {
		logger.info("Going to get Recipe Id  List from RecipeId {}", recipeId);
		List<String> list = null;

		Query query = getEntityManager().createNamedQuery("getListOfRecipeIds").setParameter(NVWorkorderConstant.RECIPE_ID,
				recipeId);
		try {
			list = query.getResultList();
		} catch (Exception e) {
			logger.error(NVWorkorderConstant.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new DaoException(e.getMessage());
		}
		logger.info(NVWorkorderConstant.RESULT_SIZE_LOGGER, list.size());
		return list;
	}


	/**
	 * Returns count of all Recipes in Db.
	 *
	 *
	 * @return Long Total Recipe count in DB
	 * @throws RestException the DaoException
	 */
	@Override
	public Long getTotalRecipeCount() {
		logger.info("Going to get Recipe Count");
		try {
			Query query = getEntityManager().createNamedQuery("getTotalRecipeCount");
			return (Long) query.getSingleResult();
		} catch (Exception e){
			logger.error(NVWorkorderConstant.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new DaoException(e.getMessage());
		}
	}


	/**
	 * Returns  List of Recipe by Category.
	 * @param category 
	 *
	 *
	 * @return  List of Recipe By category and By User
	 * @throws DaoException
	 *             the DaoException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<RecipeWrapper> getRecipeByCategory(List<String> category){
		logger.info("Going to Fetch Recipe By Category @DAO");
		try {
			Query query = getEntityManager().createNamedQuery("getRecipeByCategory");
			query.setParameter(NVWorkorderConstant.RECIPE_CATEGORY, category);
			return query.getResultList();
		} catch (Exception e){
			logger.error(NVWorkorderConstant.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new DaoException(e.getMessage());
		}
	}

	@Override
	public Integer deleteByPk(List<Integer> recipeIdList) {
		try {
			Query query = getEntityManager().createNamedQuery("deleteAllByPk");
			query.setParameter(NVWorkorderConstant.RECIPE_ID_PK, recipeIdList);
			return query.executeUpdate();
		} catch (Exception e){
			logger.error(NVWorkorderConstant.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new DaoException(e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Recipe> findAllByPkList(List<Integer> idList) {
		try {
			Query query = getEntityManager().createNamedQuery("findAllByPkList");
			query.setParameter(NVWorkorderConstant.RECIPE_ID_PK, idList);
			return query.getResultList();
		} catch (Exception e){
			logger.error(NVWorkorderConstant.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new DaoException(e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Recipe> findAllByRecipeId(List<String> recipeIdList) {
		logger.info("Going to find all recipes by recipe Id List");
		try {
			Query query = getEntityManager().createNamedQuery("findAllByRecipeId");
			query.setParameter(NVWorkorderConstant.RECIPE_ID_LIST, recipeIdList);
			return query.getResultList();
		} catch (Exception e){
			logger.error(NVWorkorderConstant.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new DaoException(e.getMessage());
		}
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<RecipeWrapper> findAllByRecipeIdList(List<String> recipeIdList) {
		logger.info("Going to find all recipes by recipe Id List");
		try {
			Query query = getEntityManager().createNamedQuery("findAllByRecipeIdList");
			query.setParameter(NVWorkorderConstant.RECIPE_ID_LIST, recipeIdList);
			return query.getResultList();
		} catch (Exception e){
			logger.error(NVWorkorderConstant.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new DaoException(e.getMessage());
		}
	}
}

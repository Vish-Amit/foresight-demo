package com.inn.foresight.module.nv.workorder.recipe.dao;

import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.module.nv.workorder.recipe.model.Recipe;
import com.inn.foresight.module.nv.workorder.recipe.wrapper.RecipeWrapper;

/** The Interface IRecipeDao. */
public interface IRecipeDao extends IGenericDao<Integer, Recipe> {	
	
	/**
	 * Find all recipies.
	 *
	 * @return the list
	 * @throws RestException the rest exception
	 * @throws DaoException 
	 */
	List<RecipeWrapper> findAllRecipes(Integer lLimit, Integer uLimit);

	/**
	 * Gets the recipe id list.
	 *
	 * @param recipeId the recipe id
	 * @return the recipe id list
	 * @throws RestException the rest exception
	 */
	List<String> getRecipeIdList(String recipeId);

	/**
	 * Returns count of all Recipes in Db.
	 *
	 *
	 * @return Long Total Recipe count in DB
	 * @throws RestException the DaoException
	 */
	Long getTotalRecipeCount();

	/**
	 * Returns List of Recipe by Category.
	 * 
	 * @param category
	 *
	 *
	 * @return List of Recipe By category and By User
	 * @throws DaoException 
	 * @throws RestException
	 *             the RestException
	 */
	List<RecipeWrapper> getRecipeByCategory(List<String> category);

	Integer deleteByPk(List<Integer> recipeIdList);

	List<Recipe> findAllByPkList(List<Integer> idList);

	List<Recipe> findAllByRecipeId(List<String> recipeIdList);

	List<RecipeWrapper> findAllByRecipeIdList(List<String> recipeIdList);

}

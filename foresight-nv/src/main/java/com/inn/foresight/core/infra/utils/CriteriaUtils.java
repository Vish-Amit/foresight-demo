package com.inn.foresight.core.infra.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.model.NEBandDetail;
import com.inn.foresight.core.infra.model.NETaskDetail;
import com.inn.foresight.core.infra.model.NetworkElement;
import com.inn.foresight.core.infra.model.RANDetail;
import com.inn.product.um.geography.model.GeographyL1;
import com.inn.product.um.geography.model.GeographyL2;
import com.inn.product.um.geography.model.GeographyL3;
import com.inn.product.um.geography.model.GeographyL4;

public class CriteriaUtils {
	private static Logger logger = LogManager.getLogger(CriteriaUtils.class);


	@SuppressWarnings("rawtypes")
	public static List<Selection<?>> getSelectionForPlannedSite(Root root, List colunmsName, List<Selection<?>> selection) {
		try {
			if (root != null && colunmsName != null && !colunmsName.isEmpty() && selection != null) {
				for (int i = 0; i < colunmsName.size(); i++) {
					if (colunmsName.get(i).toString().equalsIgnoreCase(InfraConstants.NENAME_LCASE) || colunmsName.get(i).toString().equalsIgnoreCase(InfraConstants.NE_ID)) {
						selection.add(root.get(colunmsName.get(i).toString()).alias("parent"+colunmsName.get(i).toString()));
					} else {
						selection.add(root.get(colunmsName.get(i).toString()).alias(colunmsName.get(i).toString()));
					}
				}
			}
		} catch (Exception exception) {
			logger.error("Unable to get Selections for criteria builder For Sites ", Utils.getStackTrace(exception));
		}
		return selection;
	}

	public static List<Expression<?>> getExpressionForSPMS(Map<String, Root> mapRoot, 
			Map<String, List<String>> columnNames, List<Expression<?>> expression, Boolean isGroupBy,CriteriaBuilder cb) {
		try {
			if (mapRoot != null && columnNames != null && !columnNames.isEmpty() && isGroupBy && expression != null) {
				columnNames.forEach((tableName, colunmNames) -> {
					colunmNames.forEach(colunm -> {
						if(colunm.toString().equalsIgnoreCase(InfraConstants.TP_CREATIONTIME_KEY) && isGroupBy )
						{
							expression.add(cb.function(InfraConstants.DATE,
					                Date.class,mapRoot.get(tableName).get(colunm.toString())));

						}else
						{
						expression.add(mapRoot.get(tableName).get(colunm.toString()));
						}
						
					});
				});
			}
		} catch (Exception exception) {
			logger.error("Unable to get Expression For Criteria Builder {}", Utils.getStackTrace(exception));
		}
		return expression;
	}

	
	@SuppressWarnings("rawtypes")
	public static List<Selection<?>> getSelectionForSites(Root root, List colunmsName, List<Selection<?>>
	selection) {
		try {
			if (root != null && colunmsName != null && !colunmsName.isEmpty() && selection!=null) {
				for (int i = 0; i < colunmsName.size(); i++) {
					selection.add(root.get(colunmsName.get(i).toString()).alias(colunmsName.get(i).toString()));
				}
			}
		} catch (Exception exception) {
			logger.error("Unable to get Selections for criteria builder For Sites ", Utils.getStackTrace(exception));
		}
		return selection;
	}
	
	@SuppressWarnings("rawtypes")
	public static List<Expression<?>> getExpressionForSites(Root root, List colunmsName, List<Expression<?>> expression,
			Boolean isGroupBy) {
		logger.info("Going to add criteria expression for sites");
		if (root != null && colunmsName != null && !colunmsName.isEmpty() && isGroupBy && expression !=null) {
			for (int i = 0; i < colunmsName.size(); i++) {
				expression.add(root.get(colunmsName.get(i).toString()));
			}
		}
		return expression;
	}
	
	@SuppressWarnings({ "rawtypes" })
	public static List<Predicate>  getPredicates(CriteriaBuilder builder, Root networkRoot, 
			List<Predicate> sitePredicateList, List<Map> filters) {
		if(filters !=null && builder !=null && networkRoot !=null && !filters.isEmpty())
		{
			filters.forEach(map -> {
				sitePredicateList.add((Predicate) getPredicateByOperator(builder, (String) map.get(InfraConstants.NE_OPERATION_KEY),
						map.get(InfraConstants.NE_VALUE_KEY), 
						networkRoot, (String) map.get(InfraConstants.NE_LABELTYPE_KEY),
						(String)map.get(InfraConstants.NE_DATATYPE_KEY)));
			});
		}
		return sitePredicateList;
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<Predicate> getPlannedSitePredicate(CriteriaBuilder builder, Root networkRoot, List<Predicate> sitePredicateList, List<Map> filters) {
		if (filters != null && builder != null && networkRoot != null && !filters.isEmpty()) {
			filters.forEach(map -> {
				if (map.get(InfraConstants.NE_LABELTYPE_KEY).toString().equalsIgnoreCase("neType")) {
					List value = new ArrayList<>();

					if (map.get(InfraConstants.NE_VALUE_KEY).toString().substring(0, map.get(InfraConstants.NE_VALUE_KEY).toString().lastIndexOf("_")).equalsIgnoreCase(InfraConstants.MACRO)) {
						value.add(map.get(InfraConstants.NE_VALUE_KEY).toString().substring(0, map.get(InfraConstants.NE_VALUE_KEY).toString().lastIndexOf("_")));
					} else {
						value.add(map.get(InfraConstants.NE_VALUE_KEY).toString().substring(0, map.get(InfraConstants.NE_VALUE_KEY).toString().lastIndexOf("_")).concat("_SITE"));
					}

					sitePredicateList.add((Predicate) getPredicateByOperator(builder, (String) map.get(InfraConstants.NE_OPERATION_KEY), value, networkRoot,
							(String) map.get(InfraConstants.NE_LABELTYPE_KEY), (String) map.get(InfraConstants.NE_DATATYPE_KEY)));
				} else {
					logger.info("inside planned else----------");
					sitePredicateList.add((Predicate) getPredicateByOperator(builder, (String) map.get(InfraConstants.NE_OPERATION_KEY), map.get(InfraConstants.NE_VALUE_KEY), networkRoot,
							(String) map.get(InfraConstants.NE_LABELTYPE_KEY), (String) map.get(InfraConstants.NE_DATATYPE_KEY)));
				}

			});
		}
		return sitePredicateList;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> T getPredicateByOperator(CriteriaBuilder criteriaBuilder, String operation, T value, Root root, String key, String datatype) {
		try {
			SimpleDateFormat format = new SimpleDateFormat(ForesightConstants.DATE_FORMAT_dd_MM_yy);
			if (operation.equalsIgnoreCase(InfraConstants.IN_OPERATOR))
				return (T) criteriaBuilder.upper(root.get(key)).in(getConvertedList((List) value, datatype));
			if (operation.equalsIgnoreCase(InfraConstants.EQUALS_OPERATOR)) {
				if (datatype.equalsIgnoreCase(InfraConstants.DATE_UCASE)) {
					return (T) criteriaBuilder.equal(criteriaBuilder.function(InfraConstants.DATE, Date.class, 
							root.get(key)), InfraUtils.getValueByDataType(datatype, value));
				} else if (datatype.equalsIgnoreCase(InfraConstants.TIMESTAMP)) {
					return (T) criteriaBuilder.equal(criteriaBuilder.function(InfraConstants.TIMESTAMP_UCASE, Timestamp.class, root.get(key)), new Date(Long.valueOf((String) value)));
				} else if (datatype.equalsIgnoreCase(InfraConstants.BOOLEAN)) {
					if (String.valueOf(value).equalsIgnoreCase(InfraConstants.TRUE)) {
						return (T) criteriaBuilder.equal(root.get(key), 1);
					} else if (String.valueOf(value).equalsIgnoreCase(InfraConstants.FALSE)) {
						return (T) criteriaBuilder.equal(root.get(key), 0);
					}
				} else {
					return (T) criteriaBuilder.equal(criteriaBuilder.upper(root.get(key)), InfraUtils.getValueByDataType(datatype, value));
				}
			}
			if (operation.equalsIgnoreCase(InfraConstants.LESS_THAN_OPERATOR)) {
				if (datatype.equalsIgnoreCase(InfraConstants.DATE_UCASE)) {
					return (T) criteriaBuilder.lessThan(criteriaBuilder.function(InfraConstants.DATE, Date.class, root.get(key)), format.parse((String) value));
				} else if (datatype.equalsIgnoreCase(InfraConstants.TIMESTAMP)) {
					return (T) criteriaBuilder.lessThan(criteriaBuilder.function(InfraConstants.TIMESTAMP_UCASE, Timestamp.class, root.get(key)), new Date(Long.valueOf((String) value)));
				} else {
					return (T) criteriaBuilder.lt(root.get(key), (Number) InfraUtils.getValueByDataType(datatype, value));
				}
			}
			if (operation.equalsIgnoreCase(InfraConstants.GREATOR_THAN_OPERATOR)) {
				if (datatype.equalsIgnoreCase(InfraConstants.DATE_UCASE)) {
					return (T) criteriaBuilder.greaterThan(criteriaBuilder.function(InfraConstants.DATE, Date.class, root.get(key)), format.parse((String) value));
				} else if (datatype.equalsIgnoreCase(InfraConstants.TIMESTAMP)) {
					return (T) criteriaBuilder.greaterThan(criteriaBuilder.function(InfraConstants.TIMESTAMP_UCASE, Timestamp.class, root.get(key)), new Date(Long.valueOf((String) value)));
				} else {
					return (T) criteriaBuilder.gt(root.get(key), (Number) InfraUtils.getValueByDataType(datatype, value));
				}
			}
			if (operation.equalsIgnoreCase(InfraConstants.LESS_THAN_EQUALS_OPERATOR)) {
				if (datatype.equalsIgnoreCase(InfraConstants.DATE_UCASE)) {
					return (T) criteriaBuilder.lessThanOrEqualTo(criteriaBuilder.function(InfraConstants.DATE, Date.class, root.get(key)), format.parse((String) value));
				} else if (datatype.equalsIgnoreCase(InfraConstants.TIMESTAMP)) {
					return (T) criteriaBuilder.lessThanOrEqualTo(criteriaBuilder.function(InfraConstants.TIMESTAMP_UCASE, Timestamp.class, root.get(key)), new Date(Long.valueOf((String) value)));
				} else {
					return (T) criteriaBuilder.le(root.get(key), (Number) InfraUtils.getValueByDataType(datatype, value));

				}

			}
			if (operation.equalsIgnoreCase(InfraConstants.GREATOR_THAN_EQUALS_OPERATOR)) {
				if (datatype.equalsIgnoreCase(InfraConstants.DATE_UCASE)) {
					return (T) criteriaBuilder.greaterThanOrEqualTo(criteriaBuilder.function("date", Date.class, root.get(key)), format.parse((String) value));
				} else if (datatype.equalsIgnoreCase(InfraConstants.TIMESTAMP)) {
					return (T) criteriaBuilder.greaterThanOrEqualTo(criteriaBuilder.function(InfraConstants.TIMESTAMP_UCASE, Timestamp.class, root.get(key)), new Date(Long.valueOf((String) value)));
				} else {
					return (T) criteriaBuilder.ge(root.get(key), (Number) InfraUtils.getValueByDataType(datatype, value));

				}
			}
			if (operation.equalsIgnoreCase(InfraConstants.BETWEEN_OPERATOR)) {
				String v = (String) value;
				String array[] = v.split(InfraConstants.COMMA_IDENTIFIER);
				return (T) criteriaBuilder.between(root.get(key), Double.valueOf(array[0]), Double.valueOf(array[1]));
			}

		} catch (Exception exception) {
			logger.error("Unable to get predicates by Operator {} value {} due to Exception {}  ", operation, value, Utils.getStackTrace(exception));
		}
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List getConvertedList(List tempList, String datatype) {
		List dataList = new ArrayList<>();
		tempList.forEach(s -> {
			dataList.add(InfraUtils.getValueByDataType(datatype, s));
		});
		return dataList != null && !dataList.isEmpty() ? dataList : null;
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<Predicate> getCriteriaParametersForBand(Map<String, Double> viewMap, CriteriaBuilder criteriaBuilder,
			List<Predicate> predicateList, Map<String, List<Map>> filters,
			Root<NEBandDetail> neBandDetail,  CriteriaQuery<Tuple> criteriaQuery,
			List<String> geographyList, Map map, Map<String, Root> mapRoot) {
		logger.info("Inside getCriteriaParametersForBand method");
		try {
			if (mapRoot.containsKey(InfraConstants.RANDETAIL_TABLE)) {
				predicateList.add(criteriaBuilder.equal(neBandDetail.get(InfraConstants.ID),
						mapRoot.get(InfraConstants.RANDETAIL_TABLE).get(InfraConstants.NE_BAND_DETAIL_KEY)));
			}
			if (mapRoot.containsKey(InfraConstants.NEDETAIL_TABLE)) {
				predicateList.add(criteriaBuilder.equal(neBandDetail.get(InfraConstants.NE_DETAIL), mapRoot.get(InfraConstants.NEDETAIL_TABLE).get(InfraConstants.ID)));
			}
			if (mapRoot.containsKey(InfraConstants.NELOCATION)) {
				predicateList.add(criteriaBuilder.equal(mapRoot.get(InfraConstants.NELOCATION).get(InfraConstants.ID),
						mapRoot.get(InfraConstants.NETWORKELEMENT_TABLE).get(InfraConstants.NELOCATION_LCASE)));
			}
			if (mapRoot.containsKey(InfraConstants.GEOGRAPHYL1_TABLE)) {
				predicateList = getPredicateForGeographyL1(criteriaBuilder, 
						mapRoot.get(InfraConstants.NETWORKELEMENT_TABLE), mapRoot.get(InfraConstants.GEOGRAPHYL1_TABLE),
						mapRoot.get(InfraConstants.GEOGRAPHYL2_TABLE), mapRoot.get(InfraConstants.GEOGRAPHYL3_TABLE), 
						mapRoot.get(InfraConstants.GEOGRAPHYL4_TABLE), 
						predicateList, geographyList, filters);
			} else if (mapRoot.containsKey(InfraConstants.GEOGRAPHYL2_TABLE)) {
				predicateList = getPredicateForGeographyL2(criteriaBuilder,
						mapRoot.get(InfraConstants.NETWORKELEMENT_TABLE), mapRoot.get(InfraConstants.GEOGRAPHYL2_TABLE),
						mapRoot.get(InfraConstants.GEOGRAPHYL3_TABLE), mapRoot.get(InfraConstants.GEOGRAPHYL4_TABLE), predicateList, 
						geographyList, filters);
			} else if (mapRoot.containsKey(InfraConstants.GEOGRAPHYL3_TABLE)) {
				predicateList = getPredicateForGeographyL3(criteriaBuilder,
						mapRoot.get(InfraConstants.NETWORKELEMENT_TABLE), mapRoot.get(InfraConstants.GEOGRAPHYL3_TABLE),
						mapRoot.get(InfraConstants.GEOGRAPHYL4_TABLE), predicateList, geographyList, filters);
			} else if (mapRoot.containsKey(InfraConstants.GEOGRAPHYL4_TABLE)) {
				predicateList = getPredicateForGeographyL4(criteriaBuilder, 
						mapRoot.get(InfraConstants.NETWORKELEMENT_TABLE), 
						mapRoot.get(InfraConstants.GEOGRAPHYL4_TABLE), predicateList, geographyList,
						filters);
			}
		} catch (IllegalArgumentException | NullPointerException exception) {
			logger.error("Exception caught while getting data from NetworkElement Exception : {}", Utils.getStackTrace(exception));
		} catch (Exception exception) {
			logger.error("Exception caught while getting data from NetworkElement Exception : {}", Utils.getStackTrace(exception));
		}
		return predicateList;
	}

	public static List<Predicate> getPredicateForGeographyL1(CriteriaBuilder criteriaBuilder, Root<NetworkElement> 
	elementRoot, Root<GeographyL1> geographyl1, Root<GeographyL2> geographyl2,
			Root<GeographyL3> geographyl3, Root<GeographyL4> geographyl4, List<Predicate> predicateList,
			List<String> geographyList, Map<String, List<Map>> filters) {
		try {
			predicateList.add(criteriaBuilder.equal(elementRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY), geographyl4.get(InfraConstants.ID)));
			predicateList.add(criteriaBuilder.equal(geographyl3.get(InfraConstants.ID), geographyl4.get(InfraConstants.NE_GEOGRAPHYL3_KEY)));
			predicateList.add(criteriaBuilder.equal(geographyl2.get(InfraConstants.ID), geographyl3.get(InfraConstants.NE_GEOGRAPHYL2_KEY)));
			predicateList.add(criteriaBuilder.equal(geographyl1.get(InfraConstants.ID), geographyl2.get(InfraConstants.NE_GEOGRAPHYL1_KEY)));
/*
			if(filters.get(InfraConstants.GEOGRAPHYL4_TABLE)!=null)
			predicateList = getPredicates(criteriaBuilder, geographyl4, predicateList, filters.get(InfraConstants.GEOGRAPHYL4_TABLE));

			
			if(filters.get(InfraConstants.GEOGRAPHYL3_TABLE)!=null)
			predicateList = getPredicates(criteriaBuilder, geographyl3, predicateList, filters.get(InfraConstants.GEOGRAPHYL3_TABLE));

			
			if(filters.get(InfraConstants.GEOGRAPHYL2_TABLE)!=null)
			predicateList = getPredicates(criteriaBuilder, geographyl2, predicateList, filters.get(InfraConstants.GEOGRAPHYL2_TABLE));
*/
			if (geographyList != null)
			predicateList.add((geographyl1.get(InfraConstants.NAME).in(geographyList)));
			
			/*if(filters.get(InfraConstants.GEOGRAPHYL1_TABLE)!=null)
			predicateList = getPredicates(criteriaBuilder, geographyl2, predicateList, filters.get(InfraConstants.GEOGRAPHYL1_TABLE));
			*/
			logger.info("Added predicate for Geography");

		} catch (Exception exception) {
			logger.error("Unable to get Predicate for GeographyL1", Utils.getStackTrace(exception));
		}

		return predicateList;
	}
	
	public static List<Predicate> getPredicateForGeographyL2(CriteriaBuilder criteriaBuilder, Root<NetworkElement> elementRoot, Root<GeographyL2> geographyl2, Root<GeographyL3> geographyl3,
			Root<GeographyL4> geographyl4, List<Predicate> predicateList, List<String> geographyList, Map<String, List<Map>> filters) {
		try {

			predicateList.add(criteriaBuilder.equal(elementRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY), geographyl4.get(InfraConstants.ID)));
			predicateList.add(criteriaBuilder.equal(geographyl3.get(InfraConstants.ID), geographyl4.get(InfraConstants.NE_GEOGRAPHYL3_KEY)));
			predicateList.add(criteriaBuilder.equal(geographyl2.get(InfraConstants.ID), geographyl3.get(InfraConstants.NE_GEOGRAPHYL2_KEY)));
			
			//predicateList.add(criteriaBuilder.equal(geographyl2.get(InfraConstants.ID), geographyl3.get(InfraConstants.NE_GEOGRAPHYL2_KEY)));
			if(geographyList!=null)
			predicateList.add((geographyl2.get(InfraConstants.NAME).in(geographyList)));
			//predicateList =getPredicates(criteriaBuilder, geographyl2, predicateList, filters.get(InfraConstants.GEOGRAPHYL2_TABLE));

		} catch (Exception exception) {
			logger.error("Unable to get Predicate for GeographyL2", Utils.getStackTrace(exception));
		}

		return predicateList;
	}
	
	public static List<Predicate> getPredicateForGeographyL4(CriteriaBuilder criteriaBuilder, Root<NetworkElement> elementRoot,
			Root<GeographyL4> geographyl4, List<Predicate> predicateList, List<String> geographyList, Map<String, List<Map>> filters) {
		try {
			predicateList.add(criteriaBuilder.equal(elementRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY), geographyl4.get(InfraConstants.ID)));
			if(geographyList!=null)
			predicateList.add((geographyl4.get(InfraConstants.NAME).in(geographyList)));
			//predicateList = getPredicates(criteriaBuilder, geographyl4, predicateList, filters.get(InfraConstants.GEOGRAPHYL4_TABLE));

		} catch (Exception exception) {
			logger.error("Unable to get Predicate for GeographyL4", Utils.getStackTrace(exception));
		}

		return predicateList;
	}
	
	public static List<Predicate> getPredicateForGeographyL3(CriteriaBuilder criteriaBuilder, Root elementRoot, Root<GeographyL3> geographyl3, Root<GeographyL4> geographyl4,
			List<Predicate> predicateList, List<String> geographyList, Map<String, List<Map>> filters) {
		try {

			predicateList.add(criteriaBuilder.equal(elementRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY), geographyl4.get(InfraConstants.ID)));
			//predicateList = getPredicates(criteriaBuilder, geographyl4, predicateList, filters.get(InfraConstants.GEOGRAPHYL4_TABLE));
			predicateList.add(criteriaBuilder.equal(geographyl3.get(InfraConstants.ID), geographyl4.get(InfraConstants.NE_GEOGRAPHYL3_KEY)));
			if(geographyList!=null)
			predicateList.add((geographyl3.get(InfraConstants.NAME).in(geographyList)));
			//predicateList = getPredicates(criteriaBuilder, geographyl3, predicateList, filters.get(InfraConstants.GEOGRAPHYL3_TABLE));

		} catch (Exception exception) {
			logger.error("Unable to get Predicate for GeographyL3", Utils.getStackTrace(exception));
		}
		return predicateList;
	}
	
	@SuppressWarnings({ "rawtypes" })
	public static List<Predicate> getCriteriaPredicates(CriteriaBuilder builder, Map<String, Root> mapRoot,
			List<Predicate> predicateList, Map<String, List<Map>> filters) {
		logger.info("Inside getCriteriaPredicates method {}",mapRoot);
		if (filters != null && builder != null && mapRoot != null && !filters.isEmpty()) {
			filters.forEach((tablename, filter) -> {
				filter.forEach(map -> {
					predicateList.add((Predicate) getPredicateByOperator(builder, (String) map.get(InfraConstants.NE_OPERATION_KEY),
							map.get(InfraConstants.NE_VALUE_KEY), mapRoot.get(tablename), (String) map.get(InfraConstants.NE_LABELTYPE_KEY),
							(String) map.get(InfraConstants.NE_DATATYPE_KEY)));
				});
			});
		}
		return predicateList;
	}
	
	
	@SuppressWarnings("rawtypes")
	public static List<Selection<?>> getSelectionForCriteriaBuilder(Map<String, Root> mapRoot,
			Map<String, List<String>> projection, List<Selection<?>> selection) {
		try {
			logger.info("Inside getSelectionForCriteriaBuilder method");
			if (mapRoot != null && !mapRoot.isEmpty() && projection != null && !projection.isEmpty()) {
				projection.forEach((tableName, colunmNames) -> {
					colunmNames.forEach(colunm -> {
						if (tableName.startsWith(InfraConstants.GEOGRAPHY)) {
							selection.addAll(getSelectionForGeography(mapRoot.get(tableName.toString()), colunm,
									tableName));
						} else {
							selection.add(mapRoot.get(tableName).get(colunm.toString()).alias(colunm.toString()));
						}
					});
				});
			}
		} catch (Exception exception) {			
			logger.error("Error in getting selections for criteria builder. Message : {}  ", exception.getMessage());
		}
		return selection;
	}
	
	@SuppressWarnings("rawtypes")
	public static List<Selection<?>> getSelectionForSPMS(Map<String, Root> mapRoot, Map<String, List<String>> projection, List<Selection<?>> selection, CriteriaBuilder cb) {
		try {
			logger.info("Inside getSelectionForCriteriaBuilder method");
			if (mapRoot != null && !mapRoot.isEmpty() && projection != null && !projection.isEmpty()) {
				projection.forEach((tableName, colunmNames) -> {
					colunmNames.forEach(colunm -> {
						if (tableName.startsWith(InfraConstants.GEOGRAPHY)) {
							selection.addAll(getSelectionForGeography(mapRoot.get(tableName.toString()), colunm, tableName));
						} else {
							if (colunmNames.toString().equalsIgnoreCase(InfraConstants.TP_CREATIONTIME_KEY)) {
								selection.add(cb.function(InfraConstants.DATE, Date.class, mapRoot.get(tableName).get(colunm.toString())));
							} else {
								selection.add(mapRoot.get(tableName).get(colunm.toString()).alias(colunm.toString()));
							}
						}
					});
				});
			}
		} catch (Exception exception) {
			logger.error("Error in getting selections for criteria builder. Message : {}  ", exception.getMessage());
		}
		return selection;
	}
	
	public static List<Selection<?>> getSelectionForGeography(Root rootgeography,  String projection,String tableName) {
		List<Selection<?>> selection=new ArrayList<>();
		try {
			logger.info("inside getSelectionForGeography ");
		if (rootgeography != null)
				selection = getSelectionForGeographyL4(rootgeography, projection, selection,tableName);
			
		} catch (Exception exception) {
			logger.error("Unable to get selection  for Geography", Utils.getStackTrace(exception));
		}
		
		return selection;
	}
	
	public static List<Selection<?>> getSelectionForGeographyL4(Root root, String colunmsName, List<Selection<?>> selection,String tableName) {
		try {
			logger.info("inside getSelectionForGeographyL4");
			if (root != null && colunmsName != null && !colunmsName.isEmpty()) {
					selection.add(root.get(colunmsName.toString()).alias(tableName.substring(9, 11)+colunmsName.toString()));
				
			}
		} catch (Exception exception) {
			logger.error("Unable to get Selections for criteria builder For Geographyl4 ", Utils.getStackTrace(exception));
		}
		return selection;
	}
	
	public static List<Expression<?>> getExpressionForCriteriaBuilder(Map<String, Root> mapRoot, Map<String, List<String>> columnNames, List<Expression<?>> expression, Boolean isGroupBy) {
		try {
			if (mapRoot != null && columnNames != null && !columnNames.isEmpty() && isGroupBy && expression != null) {
				columnNames.forEach((tableName, colunmNames) -> {
					colunmNames.forEach(colunm -> {
						expression.add(mapRoot.get(tableName).get(colunm.toString()));
					});
				});
			}
		} catch (Exception exception) {
			logger.error("Unable to get Expression For Criteria Builder {}", Utils.getStackTrace(exception));
		}
		return expression;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map getNESearchForPlannedSites(Root elementRoot, Map<String, Double> viewMap,
			CriteriaBuilder criteriaBuilder, List<Predicate> predicateList, Map criteriaMap,
			Root<NEBandDetail> neBandDetail, Map<String, List<Map>> filters, 
			List<Selection<?>> selection, Boolean isGroupBy, List<Expression<?>>
	expression, Map<String, List<String>> projection,
			CriteriaQuery<Tuple> criteriaQuery, List distinctEntity) {
		try {
			if (elementRoot!=null) {
				predicateList=getNEPredicate(criteriaBuilder, predicateList, elementRoot, neBandDetail);
				predicateList=getPlannedSitePredicate(criteriaBuilder, elementRoot, predicateList,
						filters.get(InfraConstants.NETWORKELEMENT_TABLE)); 
				if(viewMap!=null && !viewMap.isEmpty()) {
					predicateList.addAll(getViewPortPredicates(criteriaBuilder, elementRoot, viewMap));
				}
				selection =getSelectionForPlannedSite(elementRoot, projection.get(InfraConstants.NETWORKELEMENT_TABLE), selection);
				expression=getExpressionForSites(elementRoot, projection.get(InfraConstants.NETWORKELEMENT_TABLE), expression, isGroupBy);

				criteriaMap.put(InfraConstants.PREDICATE, predicateList);
				criteriaMap.put(InfraConstants.SELECTION, selection);
				criteriaMap.put(InfraConstants.EXPRESSION, expression);

			}
		} catch (Exception exception) {
			logger.error("Unable to get the criteria for NetworkElement for planned sites  {}", exception.getMessage());
		}
		return criteriaMap;
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map getNECriteriaForSearch(Root elementRoot, Map<String, Double> viewMap,
			CriteriaBuilder criteriaBuilder,
			List<Predicate> predicateList, Map criteriaMap,
			Root<NEBandDetail> neBandDetail, Map<String, List<Map>> filters,
			List<Selection<?>> selection, Boolean isGroupBy, 
			List<Expression<?>> expression,
			Map<String, List<String>> projection,
			CriteriaQuery<Tuple> criteriaQuery, List distinctEntity) {
		try {
			if (distinctEntity.contains(InfraConstants.NETWORKELEMENT_TABLE)) {
				predicateList = getNEPredicate(criteriaBuilder, predicateList, elementRoot, neBandDetail);
				predicateList = getPredicates(criteriaBuilder, elementRoot, predicateList, filters.get(InfraConstants.NETWORKELEMENT_TABLE));
				selection = getSelectionByViewPort(viewMap, elementRoot, projection.get(InfraConstants.NETWORKELEMENT_TABLE),
						selection, criteriaBuilder);
				expression = getExpressionForSites(elementRoot, projection.get(InfraConstants.NETWORKELEMENT_TABLE), expression, isGroupBy);
				
				criteriaMap.put(InfraConstants.PREDICATE,predicateList);
				criteriaMap.put(InfraConstants.SELECTION,selection);
				criteriaMap.put(InfraConstants.EXPRESSION,expression);

				}
		} catch (Exception exception) {
			logger.error("Unable to get the criteria for NetworkElement {}",exception.getMessage() );
		}
		return criteriaMap;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map getNECriteriaForBandDetail(Root elementRoot, Map<String, Double> viewMap,
			CriteriaBuilder criteriaBuilder,
			List<Predicate> predicateList, Map criteriaMap,
			Root<NEBandDetail> neBandDetail, Map<String, List<Map>> filters,
			List<Selection<?>> selection, Boolean isGroupBy, 
			List<Expression<?>> expression,
			Map<String, List<String>> projection,
			CriteriaQuery<Tuple> criteriaQuery, List distinctEntity) {
		try {
			if (distinctEntity.contains(InfraConstants.NETWORKELEMENT_TABLE)) {
				predicateList = getNEPredicate(criteriaBuilder, predicateList, elementRoot, neBandDetail);
				predicateList = getPredicates(criteriaBuilder, elementRoot, predicateList, filters.get(InfraConstants.NETWORKELEMENT_TABLE));
				logger.info("viewMap" + viewMap);
				selection = getSelectionForSites(elementRoot, projection.get(InfraConstants.NETWORKELEMENT_TABLE), selection);
				if(viewMap!=null && !viewMap.isEmpty()) {
					predicateList.addAll(getViewPortPredicates(criteriaBuilder, elementRoot, viewMap));
				}
				expression = getExpressionForSites(elementRoot, projection.get(InfraConstants.NETWORKELEMENT_TABLE), expression, isGroupBy);
				criteriaMap.put(InfraConstants.PREDICATE,predicateList);
				criteriaMap.put(InfraConstants.SELECTION,selection);
				criteriaMap.put(InfraConstants.EXPRESSION,expression);	

			}
		} catch (Exception exception) {
			logger.error("Unable to get the criteria for NetworkElement");
		}
		return criteriaMap;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Predicate> getViewPortPredicates(CriteriaBuilder criteriaBuilder, Root<NetworkElement> parentNetworkElement,
			Map<String, Double> viewportMap) {
		List<Predicate> predicates = new ArrayList<>();
		try {
			if (viewportMap != null && !viewportMap.isEmpty()) {
				if (viewportMap.get(InfraConstants.SW_LATITUDE) != null) {
					predicates.add(criteriaBuilder.greaterThan(parentNetworkElement.get(InfraConstants.NE_LATITUDE_KEY), viewportMap.get(InfraConstants.SW_LATITUDE)));
				}
				if (viewportMap.get(InfraConstants.NW_LAT) != null) {
					predicates.add(criteriaBuilder.lessThan(parentNetworkElement.get(InfraConstants.NE_LATITUDE_KEY), viewportMap.get(InfraConstants.NW_LAT)));
				}
				if (viewportMap.get(InfraConstants.SW_LONGITUDE) != null) {
					predicates.add(criteriaBuilder.greaterThan(parentNetworkElement.get(InfraConstants.NE_LONGITUDE_KEY), viewportMap.get(InfraConstants.SW_LONGITUDE)));
				}
				if (viewportMap.get(InfraConstants.NW_LONG) != null) {
					predicates.add(criteriaBuilder.lessThan(parentNetworkElement.get(InfraConstants.NE_LONGITUDE_KEY), viewportMap.get(InfraConstants.NW_LONG)));
				}
			}
			return predicates;
		} catch (Exception exception) {
			logger.error("Error in getting predicates for viewport for NE Message : {}", exception.getMessage());
		}
		return predicates;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Root getRootForCriteriaBuilder(String modelpath, CriteriaQuery criteriaQuery) throws ClassNotFoundException {
		try {
			if (modelpath != null) {
				return criteriaQuery.from(Class.forName(modelpath));
			}
		} catch (Exception exception) {
			logger.error("Error in getting Root for Sites for modelpath {}. Exception : {}",modelpath,Utils.getStackTrace(exception));
		}
		return null;
	}

	public static List<Predicate> getNEPredicate(CriteriaBuilder criteriaBuilder, List<Predicate> predicateList, Root<NetworkElement> 
	networkRoot, Root<NEBandDetail> neBandDetail) {
		try {
			predicateList.add(criteriaBuilder.equal(networkRoot.get(InfraConstants.ID), neBandDetail.get(InfraConstants.NE_NETWORKELEMENT_KEY)));
			predicateList.add(criteriaBuilder.equal(networkRoot.get(InfraConstants.NE_ISDELETED_KEY), 0));
			predicateList.add(networkRoot.get(InfraConstants.NE_LATITUDE_KEY).isNotNull());
			predicateList.add(networkRoot.get(InfraConstants.NE_LONGITUDE_KEY).isNotNull());
		} catch (Exception exception) {
			logger.error("Unable to get Predicate for Networkelement ", Utils.getStackTrace(exception));
		}
		return predicateList;
	}
	public static List<Selection<?>> getSelectionByViewPort(Map<String, Double> viewMap, Root<NetworkElement> elementRoot, List colunmsName, List<Selection<?>> selection,
			CriteriaBuilder criteriaBuilder) {
		try {
		if (elementRoot != null && colunmsName != null && !colunmsName.isEmpty()) {
			for (int i = 0; i < colunmsName.size(); i++) {
				if (colunmsName.get(i).toString().equalsIgnoreCase(InfraConstants.NE_LATITUDE_KEY)) {
					selection.add(criteriaBuilder
							.avg(criteriaBuilder.<Number>selectCase().when(criteriaBuilder.between(
									elementRoot.get(InfraConstants.NE_LATITUDE_KEY), viewMap.get(InfraConstants.SW_LATITUDE), viewMap.get(InfraConstants.NW_LAT)),
									elementRoot.get(InfraConstants.NE_LATITUDE_KEY)).otherwise(criteriaBuilder.nullLiteral(Number.class)))
							.alias(InfraConstants.NE_LATITUDE_KEY));

				} else if (colunmsName.get(i).toString().equalsIgnoreCase(InfraConstants.NE_LONGITUDE_KEY)) {
					selection.add(criteriaBuilder
							.avg(criteriaBuilder.<Number>selectCase().when(criteriaBuilder.
									between(elementRoot.get(InfraConstants.NE_LONGITUDE_KEY), viewMap.get(InfraConstants.SW_LONGITUDE),
											viewMap.get(InfraConstants.NW_LONG)),
									elementRoot.get(InfraConstants.NE_LONGITUDE_KEY)).otherwise(criteriaBuilder.nullLiteral(Number.class)))
							.alias(InfraConstants.NE_LONGITUDE_KEY));
				} else {
					selection.add(elementRoot.get(colunmsName.get(i).toString()).alias(colunmsName.get(i).toString()));
				}
			}
		}
		logger.info("called getSelectionForSitesForViewPort");
		}catch(Exception exception )
		{
			logger.error("Unable to get lat long");
		}
		
		return selection;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map getNEForCellDetail(Map criteriaMap, CriteriaBuilder criteriaBuilder,
			List<Predicate> predicateList, Map<String, List<Map>> filters, List<Selection<?>> selection,
			List distinctEntity, CriteriaQuery<Tuple> criteriaQuery, Map<String, List<String>> projection,
			Map<String, Double> viewPortMap, Map<String, Root> mapRoot, Root ranDetail) throws ClassNotFoundException {
		try {
			if (distinctEntity.contains(InfraConstants.NETWORKELEMENT_TABLE)) {
				Root<NetworkElement> parentElementRoot = criteriaQuery.from(NetworkElement.class);
				predicateList = getPrediacateForNetwork(criteriaBuilder, predicateList, ranDetail,
						mapRoot.get(InfraConstants.NETWORKELEMENT_TABLE), distinctEntity, filters, parentElementRoot);
				
				predicateList.addAll(getViewPortPredicates(criteriaBuilder,
						mapRoot.get(InfraConstants.NETWORKELEMENT_TABLE), viewPortMap));
				selection = getSelectionForSitesForParent(parentElementRoot, projection.get(InfraConstants.NETWORKELEMENT_TABLE),
						selection);
				selection = getSelectionForSites(mapRoot.get(InfraConstants.NETWORKELEMENT_TABLE),
						projection.get(InfraConstants.NETWORKELEMENT_TABLE), selection);

				criteriaMap.put(InfraConstants.PREDICATE, predicateList);
				criteriaMap.put(InfraConstants.SELECTION, selection);
			}
		} catch (Exception e) {
			logger.error("Exception caught in getNEForCellDetail  : {}",Utils.getStackTrace(e));

		}
		return criteriaMap;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<Predicate> getParametersForCells(Map<String, Double> viewPortMap,
			CriteriaBuilder criteriaBuilder, List<Predicate> predicateList, List distinctEntity,
			Map<String, List<Map>> filters, CriteriaQuery<Tuple> criteriaQuery, Map<String, Root> mapRoot,
			Root ranDetail) {
		try {
			logger.info("Going to get predicates for joining different entities : {}",mapRoot.keySet());
			if (mapRoot.containsKey(InfraConstants.NEBANDDETAIL_TABLE)) {
				predicateList.add(criteriaBuilder.equal(mapRoot.get(InfraConstants.NEBANDDETAIL_TABLE).get(InfraConstants.ID),
						ranDetail.get(InfraConstants.NE_BAND_DETAIL_KEY)));
			}
			
			if (mapRoot.containsKey(InfraConstants.NESECTORDETAIL)) {
				logger.info("NESectorDetail-----------------++++++++++++++>");
				predicateList.add(criteriaBuilder.equal(mapRoot.get(InfraConstants.NESECTORDETAIL).get(InfraConstants.RANDETAIL),
						ranDetail.get(InfraConstants.ID)));
				logger.info("NESectorDetail----------------->");
			}
			if (mapRoot.containsKey(InfraConstants.NELOCATION)) {
				predicateList.add(criteriaBuilder.equal(mapRoot.get(InfraConstants.NELOCATION).get(InfraConstants.ID),
						mapRoot.get(InfraConstants.NETWORKELEMENT_TABLE).get(InfraConstants.NELOCATION_LCASE)));
			}
			if (mapRoot.containsKey(InfraConstants.NETASKDETAIL_TABLE)) {
				predicateList.add(criteriaBuilder.equal(mapRoot.get(InfraConstants.NEBANDDETAIL_TABLE).get(InfraConstants.ID),
						mapRoot.get(InfraConstants.NETASKDETAIL_TABLE).get(InfraConstants.NE_BAND_DETAIL_KEY)));
			}
			if (mapRoot.containsKey(InfraConstants.NEDETAIL_TABLE)) {
				predicateList.add(criteriaBuilder.equal(mapRoot.get(InfraConstants.NEBANDDETAIL_TABLE).get(InfraConstants.NE_DETAIL),
						mapRoot.get(InfraConstants.NEDETAIL_TABLE).get(InfraConstants.ID)));
			}
			if (mapRoot.containsKey(InfraConstants.GEOGRAPHYL1_TABLE)) {
				predicateList = getPredicateForGeographyL1(criteriaBuilder,
						mapRoot.get(InfraConstants.NETWORKELEMENT_TABLE), mapRoot.get(InfraConstants.GEOGRAPHYL1_TABLE),
						mapRoot.get(InfraConstants.GEOGRAPHYL2_TABLE), mapRoot.get(InfraConstants.GEOGRAPHYL3_TABLE),
						mapRoot.get(InfraConstants.GEOGRAPHYL4_TABLE), predicateList, null, filters);
			} else if (mapRoot.containsKey(InfraConstants.GEOGRAPHYL2_TABLE)) {
				predicateList = getPredicateForGeographyL2(criteriaBuilder,
						mapRoot.get(InfraConstants.NETWORKELEMENT_TABLE), mapRoot.get(InfraConstants.GEOGRAPHYL2_TABLE),
						mapRoot.get(InfraConstants.GEOGRAPHYL3_TABLE), mapRoot.get(InfraConstants.GEOGRAPHYL4_TABLE),
						predicateList, null, filters);
			} else if (mapRoot.containsKey(InfraConstants.GEOGRAPHYL3_TABLE)) {
				predicateList = getPredicateForGeographyL3(criteriaBuilder,
						mapRoot.get(InfraConstants.NETWORKELEMENT_TABLE), mapRoot.get(InfraConstants.GEOGRAPHYL3_TABLE),
						mapRoot.get(InfraConstants.GEOGRAPHYL4_TABLE), predicateList, null, filters);
			} else if (mapRoot.containsKey(InfraConstants.GEOGRAPHYL4_TABLE)) {
				predicateList = getPredicateForGeographyL4(criteriaBuilder,
						mapRoot.get(InfraConstants.NETWORKELEMENT_TABLE), mapRoot.get(InfraConstants.GEOGRAPHYL4_TABLE),
						predicateList, null, filters);
			}
		} catch (IllegalArgumentException | NullPointerException exception) {
			logger.error("Exception caught while getting data from NetworkElement Exception : {}",Utils.getStackTrace(exception));
		} catch (Exception exception) {
			logger.error("Exception caught while getting data from NetworkElement Exception : {}",Utils.getStackTrace(exception));
		}
		return predicateList;
	}
	
	@SuppressWarnings("rawtypes")
	public static Map<String, Root> getRootMap(List<String> distinctEntity, CriteriaQuery criteriaQuery) throws ClassNotFoundException {
		Map<String, Root> map = new HashMap<>();
		distinctEntity.forEach(s -> {
			try {
				logger.info("S======================:{}",s);
				if (!s.equalsIgnoreCase(InfraConstants.PARENT_NETWORK_ELEMENT)) {
					if (!s.startsWith(InfraConstants.GEOGRAPHY)) {
						if (s.equalsIgnoreCase(InfraConstants.NESECTORDETAIL) || s.equalsIgnoreCase("CustomNEDetail")) {
							map.put(s, getRootForCriteriaBuilder(InfraConstants.MODEL_PACKAGE_PATH + s, criteriaQuery));
							logger.info("NESectorDetail {} ", map);
						} else {
							map.put(s, getRootForCriteriaBuilder(InfraConstants.MODEL_PACKAGE_PATH + s, criteriaQuery));
						}
					} else {
						map.put(s, getRootForCriteriaBuilder(InfraConstants.MODEL_GEOGRAPHY_PACKAGE_PATH + s, criteriaQuery));

					}
				}
			} catch (ClassNotFoundException e) {
				logger.error("Error in getting root for Sites");
			}
		});
		return map;
	}

	public static List<Predicate> getPrediacateForNetwork(CriteriaBuilder criteriaBuilder, List<Predicate> predicateList,
			Root<RANDetail> ranDetailRoot, Root<NetworkElement> childElementRoot,
			List distinctEntity, Map<String, List<Map>> filters, Root<NetworkElement> parentElementRoot) {
		try {
			predicateList.add(criteriaBuilder.equal(parentElementRoot.get(InfraConstants.ID), childElementRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY)));
			if (ranDetailRoot != null) {
				predicateList.add(criteriaBuilder.equal(childElementRoot.get(InfraConstants.ID), ranDetailRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY)));
			}
			predicateList.add(criteriaBuilder.equal(childElementRoot.get(InfraConstants.NE_ISDELETED_KEY), 0));
			predicateList.add(parentElementRoot.get(InfraConstants.NE_LATITUDE_KEY).isNotNull());
			predicateList.add(parentElementRoot.get(InfraConstants.NE_LONGITUDE_KEY).isNotNull());
			if (distinctEntity.contains(InfraConstants.PARENT_NETWORK_ELEMENT)) {
				predicateList = getPredicates(criteriaBuilder, parentElementRoot, predicateList,
						filters.get(InfraConstants.PARENT_NETWORK_ELEMENT));
					if (distinctEntity.contains(InfraConstants.NETWORKELEMENT_TABLE)) {
						predicateList = getPredicates(criteriaBuilder, childElementRoot, predicateList, 
								filters.get(InfraConstants.NETWORKELEMENT_TABLE));
					}
			} else {
				predicateList = getPredicates(criteriaBuilder, childElementRoot, predicateList, filters.get(InfraConstants.NETWORKELEMENT_TABLE));
			}
		} catch (Exception exception) {
			logger.error("Unable to get Predicate for Networkelement data", Utils.getStackTrace(exception));
		}
		return predicateList;
	}
	
	
	public static List<Selection<?>> getSelectionForSitesForParent(Root root, List colunmsName, List<Selection<?>> selection) {
		if (root != null && colunmsName != null && !colunmsName.isEmpty()) {
			for (int i = 0; i < colunmsName.size(); i++) {
				selection.add(root.get(colunmsName.get(i).toString()).alias("parent"+colunmsName.get(i).toString()));
			}
		}
		return selection;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> Map getFilterMapForInputParameters(String labelType,String operation,String dataType,T value) {
		Map map = new HashMap();
		try {
			map.put(InfraConstants.NE_LABELTYPE_KEY,labelType);
			map.put(InfraConstants.NE_OPERATION_KEY,operation);
			map.put(InfraConstants.NE_DATATYPE_KEY,dataType);
			map.put(InfraConstants.NE_VALUE_KEY,value);
		}catch(Exception exception) {
			logger.error("Error in getting filter map for input parameters. Message : {}",exception.getMessage());
		} 
		return map;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map getNetworkElementDetails(Map criteriaMap, CriteriaBuilder criteriaBuilder,
			List<Predicate> predicateList, Map<String, List<Map>> filters, List<Selection<?>> selection,
			List distinctEntity, CriteriaQuery<Tuple> criteriaQuery, Map<String, List<String>> projection,
			Map<String, Double> viewPortMap, Map<String, Root> mapRoot, Root ranDetail) throws ClassNotFoundException {
		try {
			if (distinctEntity.contains(InfraConstants.NETWORKELEMENT_TABLE)) {
				predicateList = getPredicateForNetworkElement(criteriaBuilder, predicateList, ranDetail,
						mapRoot.get(InfraConstants.NETWORKELEMENT_TABLE), distinctEntity, filters);
				predicateList.addAll(getViewPortPredicates(criteriaBuilder,
						mapRoot.get(InfraConstants.NETWORKELEMENT_TABLE), viewPortMap));
				selection = getSelectionForSites(mapRoot.get(InfraConstants.NETWORKELEMENT_TABLE),
						projection.get(InfraConstants.NETWORKELEMENT_TABLE), selection);
				criteriaMap.put(InfraConstants.PREDICATE, predicateList);
				criteriaMap.put(InfraConstants.SELECTION, selection);
			}
		} catch (Exception e) {
			logger.error("Exception caught in getNetworkElementDetails. Exception : {}",Utils.getStackTrace(e));

		}
		return criteriaMap;
	}
	
	
	public static List<Predicate> getPredicateForNetworkElement(CriteriaBuilder criteriaBuilder, List<Predicate> predicateList, Root<RANDetail> ranDetailRoot, Root<NetworkElement> childElementRoot,
			List distinctEntity, Map<String, List<Map>> filters) {
		try {
			predicateList.add(criteriaBuilder.equal(childElementRoot.get(InfraConstants.NE_ISDELETED_KEY), 0));
			predicateList = getPredicates(criteriaBuilder, childElementRoot, predicateList, filters.get(InfraConstants.NETWORKELEMENT_TABLE));
		} catch (Exception exception) {
			logger.error("Unable to get Predicate for Networkelement data", Utils.getStackTrace(exception));
		}
		return predicateList;
	}

	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<Predicate> getParametersForNewtorkElements(Map<String, Double> viewPortMap,
			CriteriaBuilder criteriaBuilder, List<Predicate> predicateList, List distinctEntity,
			Map<String, List<Map>> filters, CriteriaQuery<Tuple> criteriaQuery, Map<String, Root> mapRoot,
			Root ranDetail) {
		try {
			logger.info("Going to get predicates for joining different entities : {}", mapRoot.keySet());
			if (mapRoot.containsKey(InfraConstants.NEBANDDETAIL_TABLE) && mapRoot.containsKey(InfraConstants.RANDETAIL_TABLE)) {
				predicateList.add(
						criteriaBuilder.equal(mapRoot.get(InfraConstants.NEBANDDETAIL_TABLE).get(InfraConstants.ID),
								ranDetail.get(InfraConstants.NE_BAND_DETAIL_KEY)));
			}
			if (mapRoot.containsKey(InfraConstants.NEBANDDETAIL_TABLE) && mapRoot.containsKey(InfraConstants.NETWORKELEMENT_TABLE)) {
				predicateList.add(
						criteriaBuilder.equal(mapRoot.get(InfraConstants.NEBANDDETAIL_TABLE).get(InfraConstants.NE_NETWORKELEMENT_KEY),
								mapRoot.get(InfraConstants.NETWORKELEMENT_TABLE).get(InfraConstants.ID)));
			}
			if (mapRoot.containsKey(InfraConstants.NELOCATION)) {
				predicateList.add(criteriaBuilder.equal(mapRoot.get(InfraConstants.NELOCATION).get(InfraConstants.ID),
						mapRoot.get(InfraConstants.NETWORKELEMENT_TABLE).get(InfraConstants.NELOCATION_LCASE)));
			}
			if (mapRoot.containsKey(InfraConstants.NETASKDETAIL_TABLE)) {
				predicateList.add(
						criteriaBuilder.equal(mapRoot.get(InfraConstants.NEBANDDETAIL_TABLE).get(InfraConstants.ID),
								mapRoot.get(InfraConstants.NETASKDETAIL_TABLE).get(InfraConstants.NE_BAND_DETAIL_KEY)));
			}
			if (mapRoot.containsKey(InfraConstants.NEDETAIL_TABLE)) {
				predicateList.add(criteriaBuilder.equal(
						mapRoot.get(InfraConstants.NEDETAIL_TABLE).get(InfraConstants.NE_NETWORKELEMENT_KEY),
						mapRoot.get(InfraConstants.NETWORKELEMENT_TABLE).get(InfraConstants.ID)));
			}
			if (mapRoot.containsKey(InfraConstants.GEOGRAPHYL1_TABLE)) {
				predicateList = getPredicateForGeographyL1(criteriaBuilder,
						mapRoot.get(InfraConstants.NETWORKELEMENT_TABLE), mapRoot.get(InfraConstants.GEOGRAPHYL1_TABLE),
						mapRoot.get(InfraConstants.GEOGRAPHYL2_TABLE), mapRoot.get(InfraConstants.GEOGRAPHYL3_TABLE),
						mapRoot.get(InfraConstants.GEOGRAPHYL4_TABLE), predicateList, null, filters);
			} else if (mapRoot.containsKey(InfraConstants.GEOGRAPHYL2_TABLE)) {
				predicateList = getPredicateForGeographyL2(criteriaBuilder,
						mapRoot.get(InfraConstants.NETWORKELEMENT_TABLE), mapRoot.get(InfraConstants.GEOGRAPHYL2_TABLE),
						mapRoot.get(InfraConstants.GEOGRAPHYL3_TABLE), mapRoot.get(InfraConstants.GEOGRAPHYL4_TABLE),
						predicateList, null, filters);
			} else if (mapRoot.containsKey(InfraConstants.GEOGRAPHYL3_TABLE)) {
				predicateList = getPredicateForGeographyL3(criteriaBuilder,
						mapRoot.get(InfraConstants.NETWORKELEMENT_TABLE), mapRoot.get(InfraConstants.GEOGRAPHYL3_TABLE),
						mapRoot.get(InfraConstants.GEOGRAPHYL4_TABLE), predicateList, null, filters);
			} else if (mapRoot.containsKey(InfraConstants.GEOGRAPHYL4_TABLE)) {
				predicateList = getPredicateForGeographyL4(criteriaBuilder,
						mapRoot.get(InfraConstants.NETWORKELEMENT_TABLE), mapRoot.get(InfraConstants.GEOGRAPHYL4_TABLE),
						predicateList, null, filters);
			}
		} catch (IllegalArgumentException | NullPointerException exception) {
			logger.error("Exception caught while getting data from NetworkElement Exception : {}",
					Utils.getStackTrace(exception));
		} catch (Exception exception) {
			logger.error("Exception caught while getting data from NetworkElement Exception : {}",
					Utils.getStackTrace(exception));
		}
		return predicateList;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map getNECriteriaForData(Root elementRoot,CriteriaBuilder criteriaBuilder,
			List<Predicate> predicateList, Map criteriaMap,
			Root<NEBandDetail> neBandDetail, Map<String, List<Map>> filters,
			List<Selection<?>> selection,List<Expression<?>> expression,
			Map<String, List<String>> projection,
			CriteriaQuery<Tuple> criteriaQuery, List distinctEntity) {
		try {
			if (distinctEntity.contains(InfraConstants.NETWORKELEMENT_TABLE)) {
				predicateList = getNEPredicate(criteriaBuilder, predicateList, elementRoot, neBandDetail);
				predicateList = getPredicates(criteriaBuilder, elementRoot, predicateList, filters.get(InfraConstants.NETWORKELEMENT_TABLE));
				selection = getSelectionForData(elementRoot, projection.get(InfraConstants.NETWORKELEMENT_TABLE),selection, criteriaBuilder);
				expression = getExpressionForData(elementRoot, projection.get(InfraConstants.NETWORKELEMENT_TABLE), expression);
				criteriaMap.put(InfraConstants.PREDICATE,predicateList);
				criteriaMap.put(InfraConstants.SELECTION,selection);
				criteriaMap.put(InfraConstants.EXPRESSION,expression);

				}
		} catch (Exception exception) {
			logger.error("Unable to get the criteria for NetworkElement {}",exception.getMessage() );
		}
		return criteriaMap;
	}
	public static Map getNECriteriaForNETaskDetailData(Root elementRoot,CriteriaBuilder criteriaBuilder,
			List<Predicate> predicateList, Map criteriaMap,
			Root<NETaskDetail> neTaskDetail, Map<String, List<Map>> filters,
			List<Selection<?>> selection,List<Expression<?>> expression,
			Map<String, List<String>> projection,
			CriteriaQuery<Tuple> criteriaQuery, List distinctEntity,Root<NEBandDetail> neBandDetailRoot) {
		try {
			predicateList.add(criteriaBuilder.equal(neBandDetailRoot.get(InfraConstants.ID), neTaskDetail.get(InfraConstants.NE_BANDDETAIL_KEY)));
			if (distinctEntity.contains(InfraConstants.NETWORKELEMENT_TABLE)) {
				predicateList = getNEPredicateForTaskDetailData(criteriaBuilder, predicateList, elementRoot, neTaskDetail,neBandDetailRoot);
				predicateList = getPredicates(criteriaBuilder, elementRoot, predicateList, filters.get(InfraConstants.NETWORKELEMENT_TABLE));
				selection = getSelectionForData(elementRoot, projection.get(InfraConstants.NETWORKELEMENT_TABLE),selection, criteriaBuilder);
				expression = getExpressionForData(elementRoot, projection.get(InfraConstants.NETWORKELEMENT_TABLE), expression);
				criteriaMap.put(InfraConstants.PREDICATE,predicateList);
				criteriaMap.put(InfraConstants.SELECTION,selection);
				criteriaMap.put(InfraConstants.EXPRESSION,expression);

				}
		} catch (Exception exception) {
			logger.error("Unable to get the criteria for NetworkElement {}",exception.getMessage() );
		}
		logger.info("necriteria set successfully");
		return criteriaMap;
	}
	public static List<Selection<?>> getSelectionForData(Root<NetworkElement> elementRoot, List colunmsName, List<Selection<?>> selection,
			CriteriaBuilder criteriaBuilder) {
		try {
		if (elementRoot != null && colunmsName != null && !colunmsName.isEmpty()) {
			for (int i = 0; i < colunmsName.size(); i++) {
					selection.add(elementRoot.get(colunmsName.get(i).toString()).alias(colunmsName.get(i).toString()));
			}
		}
		}catch(Exception exception )
		{
			logger.error("Unable to get lat long");
		}
		
		return selection;
	}
	@SuppressWarnings("rawtypes")
	public static List<Expression<?>> getExpressionForData(Root root, List colunmsName, List<Expression<?>> expression) {
		if (root != null && colunmsName != null && !colunmsName.isEmpty() && expression !=null) {
			for (int i = 0; i < colunmsName.size(); i++) {
				expression.add(root.get(colunmsName.get(i).toString()));
			}
		}
		return expression;
	}
	public static List<Expression<?>> getExpressionForCB(Map<String, Root> mapRoot, Map<String, List<String>> columnNames, List<Expression<?>> expression) {
		try {
			if (mapRoot != null && columnNames != null && !columnNames.isEmpty() && expression != null) {
				columnNames.forEach((tableName, colunmNames) -> {
					colunmNames.forEach(colunm -> {
						expression.add(mapRoot.get(tableName).get(colunm.toString()));
					});
				});
			}
		} catch (Exception exception) {
			logger.error("Unable to get Expression For Criteria Builder {}", Utils.getStackTrace(exception));
		}
		return expression;
	}
	public static List<Predicate> getNEPredicateForTaskDetailData(CriteriaBuilder criteriaBuilder, List<Predicate> predicateList, Root<NetworkElement> 
	networkRoot, Root<NETaskDetail> neTaskDetail,Root<NEBandDetail> neBandDetailRoot) {
		try {
			predicateList.add(criteriaBuilder.equal(networkRoot.get(InfraConstants.ID), neBandDetailRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY)));
			predicateList.add(criteriaBuilder.equal(networkRoot.get(InfraConstants.NE_ISDELETED_KEY), 0));
			predicateList.add(networkRoot.get(InfraConstants.NE_LATITUDE_KEY).isNotNull());
			predicateList.add(networkRoot.get(InfraConstants.NE_LONGITUDE_KEY).isNotNull());
		} catch (Exception exception) {
			logger.error("Unable to get Predicate for Networkelement ", Utils.getStackTrace(exception));
		}
		return predicateList;
	}
	

	public static List<Predicate> getRANPlanJoin(CriteriaBuilder criteriaBuilder, Map<String, List<Map>> filters,
			List<Predicate> predicateList, CriteriaQuery<Tuple> criteriaQuery, Map<String, Root> rootMap) {
		logger.info("inside getRANPlanJoin");
		if (rootMap.containsKey(InfraConstants.NETWORKELEMENT_TABLE)
				&& rootMap.containsKey(InfraConstants.NELOCATION)) {
			predicateList.add(criteriaBuilder.equal(rootMap.get(InfraConstants.NETWORKELEMENT_TABLE).get("neLocation"),
					rootMap.get(InfraConstants.NELOCATION).get("id")));
			logger.info("Inside  1");
		}

		if (rootMap.containsKey(InfraConstants.NETWORKELEMENT_TABLE)
				&& rootMap.containsKey(InfraConstants.NEDETAIL_TABLE)) {
			predicateList.add(criteriaBuilder.equal(rootMap.get(InfraConstants.NETWORKELEMENT_TABLE).get("id"),
					rootMap.get(InfraConstants.NEDETAIL_TABLE).get("networkElement")));
			logger.info("Inside  111");
		}

		if (rootMap.containsKey(InfraConstants.NETWORKELEMENT_TABLE)
				&& rootMap.containsKey(InfraConstants.NEBANDDETAIL_TABLE)) {
			predicateList.add(criteriaBuilder.equal(rootMap.get(InfraConstants.NETWORKELEMENT_TABLE).get("id"),
					rootMap.get(InfraConstants.NEBANDDETAIL_TABLE).get("networkElement")));
			logger.info("Inside  111");
		}

		if (rootMap.containsKey(InfraConstants.NETWORKELEMENT_TABLE) && rootMap.containsKey("CustomNEDetail")) {
			predicateList.add(criteriaBuilder.equal(rootMap.get("CustomNEDetail").get("networkElement"),
					rootMap.get(InfraConstants.NETWORKELEMENT_TABLE).get("id")));
			logger.info("Inside  2");
		}
		if (rootMap.containsKey(InfraConstants.NETWORKELEMENT_TABLE)) {
			predicateList.add(criteriaBuilder
					.equal(rootMap.get(InfraConstants.NETWORKELEMENT_TABLE).get(InfraConstants.ISDELETED_STATUS), 0));
			logger.info("Inside  3");
		}
		if (rootMap.containsKey(InfraConstants.GEOGRAPHYL1_TABLE)) {
			predicateList.add(criteriaBuilder.equal(rootMap.get(InfraConstants.NETWORKELEMENT_TABLE).get("geographyL1"),
					rootMap.get(InfraConstants.GEOGRAPHYL1_TABLE).get("id")));
		}
		if (rootMap.containsKey(InfraConstants.GEOGRAPHYL2_TABLE)) {
			predicateList.add(criteriaBuilder.equal(rootMap.get(InfraConstants.NETWORKELEMENT_TABLE).get("geographyL2"),
					rootMap.get(InfraConstants.GEOGRAPHYL2_TABLE).get("id")));
		}
		if (rootMap.containsKey(InfraConstants.GEOGRAPHYL3_TABLE)) {
			predicateList.add(criteriaBuilder.equal(rootMap.get(InfraConstants.NETWORKELEMENT_TABLE).get("geographyL3"),
					rootMap.get(InfraConstants.GEOGRAPHYL3_TABLE).get("id")));
		}
		if (rootMap.containsKey(InfraConstants.GEOGRAPHYL4_TABLE)) {
			predicateList.add(criteriaBuilder.equal(rootMap.get(InfraConstants.NETWORKELEMENT_TABLE).get("geographyL4"),
					rootMap.get(InfraConstants.GEOGRAPHYL4_TABLE).get("id")));
		}
		return predicateList;
	}
	
	
	public static void setPagination(Integer lLimit, Integer uLimit, Query query) {
		logger.info("Going to set pagination lLimit {}  uLimit {}",lLimit,uLimit);
		if (lLimit != null && uLimit != null && lLimit >= 0 && uLimit > 0) {
			query.setMaxResults(uLimit - lLimit + 1);
			query.setFirstResult(lLimit);
		}
	}
	
	
}




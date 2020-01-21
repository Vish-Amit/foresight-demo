package com.inn.foresight.core.scatterchart.wrapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class ScatterChartData.
 */
public class ScatterChartData implements Serializable {

	/**
     *
     */
    private static final long serialVersionUID = -2558118831716243318L;

    /** The min and max map. */
	private Map<String, Double> minAndMaxMap = new HashMap<>();

	/** The first quadrant count. */
	private long firstQuadrantCount;

	/** The second quadrant count. */
	private long secondQuadrantCount;

	/** The third quadrant count. */
	private long thirdQuadrantCount;

	/** The fourth quadrant count. */
	private long fourthQuadrantCount;

	/** The quad first points. */
	private List<List<Double>> quadFirstPoints = new ArrayList<>();

	/** The quad second points. */
	private List<List<Double>> quadSecondPoints = new ArrayList<>();

	/** The quad third points. */
	private List<List<Double>> quadThirdPoints = new ArrayList<>();

	/** The quad fourth points. */
	private List<List<Double>> quadFourthPoints = new ArrayList<>();

	/** The sum of percentage. */
	private double sumOfPercentage;

	/** The x points. */
	private List<Double> xPoints = new ArrayList<>();

	/** The y points. */
	private List<Double> yPoints = new ArrayList<>();

	/** The x total. */
	private double xTotal;

	/** The y total. */
	private double yTotal;

	/** The x. */
	private double x;

	/** The y. */
	private double y;

	/** The sum of XY. */
	private double sumOfXY;

	/** The sum of X square. */
	private double sumOfXSquare;

	/** The kpi value map. */
	private Map<String, Boolean> kpiValueMap;

	/**
	 * Gets the kpi value map.
	 *
	 * @return the kpi value map
	 */
	public Map<String, Boolean> getKpiValueMap() {
		return kpiValueMap;
	}

	/**
	 * Sets the kpi value map.
	 *
	 * @param kpiValueMap the kpi value map
	 */
	public void setKpiValueMap(Map<String, Boolean> kpiValueMap) {
		this.kpiValueMap = kpiValueMap;
	}

	/**
	 * Gets the min and max map.
	 *
	 * @return the min and max map
	 */
	public Map<String, Double> getMinAndMaxMap() {
		return minAndMaxMap;
	}

	/**
	 * Sets the min and max map.
	 *
	 * @param minAndMaxMap the min and max map
	 */
	public void setMinAndMaxMap(Map<String, Double> minAndMaxMap) {
		this.minAndMaxMap = minAndMaxMap;
	}

	/**
	 * Gets the first quadrant count.
	 *
	 * @return the first quadrant count
	 */
	public long getFirstQuadrantCount() {
		return firstQuadrantCount;
	}

	/**
	 * Sets the first quadrant count.
	 *
	 * @param firstQuadrantCount the new first quadrant count
	 */
	public void setFirstQuadrantCount(long firstQuadrantCount) {
		this.firstQuadrantCount = firstQuadrantCount;
	}

	/**
	 * Gets the second quadrant count.
	 *
	 * @return the second quadrant count
	 */
	public long getSecondQuadrantCount() {
		return secondQuadrantCount;
	}

	/**
	 * Sets the second quadrant count.
	 *
	 * @param secondQuadrantCount the new second quadrant count
	 */
	public void setSecondQuadrantCount(long secondQuadrantCount) {
		this.secondQuadrantCount = secondQuadrantCount;
	}

	/**
	 * Gets the third quadrant count.
	 *
	 * @return the third quadrant count
	 */
	public long getThirdQuadrantCount() {
		return thirdQuadrantCount;
	}

	/**
	 * Sets the third quadrant count.
	 *
	 * @param thirdQuadrantCount the new third quadrant count
	 */
	public void setThirdQuadrantCount(long thirdQuadrantCount) {
		this.thirdQuadrantCount = thirdQuadrantCount;
	}

	/**
	 * Gets the fourth quadrant count.
	 *
	 * @return the fourth quadrant count
	 */
	public long getFourthQuadrantCount() {
		return fourthQuadrantCount;
	}

	/**
	 * Sets the fourth quadrant count.
	 *
	 * @param fourthQuadrantCount the new fourth quadrant count
	 */
	public void setFourthQuadrantCount(long fourthQuadrantCount) {
		this.fourthQuadrantCount = fourthQuadrantCount;
	}

	/**
	 * Gets the quad first points.
	 *
	 * @return the quad first points
	 */
	public List<List<Double>> getQuadFirstPoints() {
		return quadFirstPoints;
	}

	/**
	 * Sets the quad first points.
	 *
	 * @param quadFirstPoints the new quad first points
	 */
	public void setQuadFirstPoints(List<List<Double>> quadFirstPoints) {
		this.quadFirstPoints = quadFirstPoints;
	}

	/**
	 * Gets the quad second points.
	 *
	 * @return the quad second points
	 */
	public List<List<Double>> getQuadSecondPoints() {
		return quadSecondPoints;
	}

	/**
	 * Sets the quad second points.
	 *
	 * @param quadSecondPoints the new quad second points
	 */
	public void setQuadSecondPoints(List<List<Double>> quadSecondPoints) {
		this.quadSecondPoints = quadSecondPoints;
	}

	/**
	 * Gets the quad third points.
	 *
	 * @return the quad third points
	 */
	public List<List<Double>> getQuadThirdPoints() {
		return quadThirdPoints;
	}

	/**
	 * Sets the quad third points.
	 *
	 * @param quadThirdPoints the new quad third points
	 */
	public void setQuadThirdPoints(List<List<Double>> quadThirdPoints) {
		this.quadThirdPoints = quadThirdPoints;
	}

	/**
	 * Gets the quad fourth points.
	 *
	 * @return the quad fourth points
	 */
	public List<List<Double>> getQuadFourthPoints() {
		return quadFourthPoints;
	}

	/**
	 * Sets the quad fourth points.
	 *
	 * @param quadFourthPoints the new quad fourth points
	 */
	public void setQuadFourthPoints(List<List<Double>> quadFourthPoints) {
		this.quadFourthPoints = quadFourthPoints;
	}

	/**
	 * Gets the sum of percentage.
	 *
	 * @return the sum of percentage
	 */
	public double getSumOfPercentage() {
		return sumOfPercentage;
	}

	/**
	 * Sets the sum of percentage.
	 *
	 * @param sumOfPercentage the new sum of percentage
	 */
	public void setSumOfPercentage(double sumOfPercentage) {
		this.sumOfPercentage = sumOfPercentage;
	}

	/**
	 * Gets the x points.
	 *
	 * @return the x points
	 */
	public List<Double> getxPoints() {
		return xPoints;
	}

	/**
	 * Gets the x total.
	 *
	 * @return the x total
	 */
	public double getxTotal() {
		return xTotal;
	}

	/**
	 * Gets the y total.
	 *
	 * @return the y total
	 */
	public double getyTotal() {
		return yTotal;
	}

	/**
	 * Gets the x.
	 *
	 * @return the x
	 */
	public double getX() {
		return x;
	}

	/**
	 * Sets the x.
	 *
	 * @param x the new x
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Gets the y.
	 *
	 * @return the y
	 */
	public double getY() {
		return y;
	}

	/**
	 * Sets the y.
	 *
	 * @param y the new y
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * Gets the sum of XY.
	 *
	 * @return the sum of XY
	 */
	public double getSumOfXY() {
		return sumOfXY;
	}

	/**
	 * Sets the sum of XY.
	 *
	 * @param sumOfXY the new sum of XY
	 */
	public void setSumOfXY(double sumOfXY) {
		this.sumOfXY = sumOfXY;
	}

	/**
	 * Gets the sum of X square.
	 *
	 * @return the sum of X square
	 */
	public double getSumOfXSquare() {
		return sumOfXSquare;
	}

	/**
	 * Sets the sum of X square.
	 *
	 * @param sumOfXSquare the new sum of X square
	 */
	public void setSumOfXSquare(double sumOfXSquare) {
		this.sumOfXSquare = sumOfXSquare;
	}

	/**
	 * Increment first quadrant.
	 */
	public void incrementFirstQuadrant() {
		this.firstQuadrantCount++;
	}

	/**
	 * Increment second quadrant.
	 */
	public void incrementSecondQuadrant() {
		this.secondQuadrantCount++;
	}

	/**
	 * Increment third quadrant.
	 */
	public void incrementThirdQuadrant() {
		this.thirdQuadrantCount++;
	}

	/**
	 * Increment fourth quadrant.
	 */
	public void incrementFourthQuadrant() {
		this.fourthQuadrantCount++;
	}

	/**
	 * Adds the first quad points.
	 *
	 * @param values the values
	 */
	public void addFirstQuadPoints(List<Double> values) {
		if (values != null && !values.isEmpty()) {
			this.quadFirstPoints.add(values);
		}
	}

	/**
	 * Adds the second quad points.
	 *
	 * @param values the values
	 */
	public void addSecondQuadPoints(List<Double> values) {
		if (values != null && !values.isEmpty()) {
			this.quadSecondPoints.add(values);
		}
	}

	/**
	 * Adds the third quad points.
	 *
	 * @param values the values
	 */
	public void addThirdQuadPoints(List<Double> values) {
		if (values != null && !values.isEmpty()) {
			this.quadThirdPoints.add(values);
		}
	}

	/**
	 * Adds the fourth quad points.
	 *
	 * @param values the values
	 */
	public void addFourthQuadPoints(List<Double> values) {
		if (values != null && !values.isEmpty()) {
			this.quadFourthPoints.add(values);
		}
	}

	/**
	 * Adds the xpoint.
	 *
	 * @param x the x
	 */
	public void addXpoint(Double x) {
		if (x != null) {
			this.xPoints.add(x);
		}
	}

	/**
	 * Adds the ypoint.
	 *
	 * @param y the y
	 */
	public void addYpoint(Double y) {
		if (y != null) {
			this.yPoints.add(y);
		}
	}

	/**
	 * Aggregate X.
	 *
	 * @param x the x
	 */
	public void aggregateX(Double x) {
		if (x != null) {
			this.xTotal += x;
		}
	}

	/**
	 * Aggregate Y.
	 *
	 * @param y the y
	 */
	public void aggregateY(Double y) {
		if (y != null) {
			this.yTotal += y;
		}
	}

	/**
	 * Aggregate X square sum.
	 *
	 * @param x the x
	 */
	public void aggregateXSquareSum(Double x) {
		if (x != null) {
			this.sumOfXSquare += Math.pow(x, 2);
		}
	}

	/**
	 * Aggregate sum of XY.
	 *
	 * @param x the x
	 * @param y the y
	 */
	public void aggregateSumOfXY(Double x, Double y) {
		if (x != null && y != null) {
			this.sumOfXY += (x * y);
		}
	}

	/**
	 * Gets the all quadrant count.
	 *
	 * @return the all quadrant count
	 */
	public Long getAllQuadrantCount() {
		return firstQuadrantCount + secondQuadrantCount + thirdQuadrantCount + fourthQuadrantCount;
	}

	/**
	 * Aggregate quad percentage.
	 *
	 * @param percent the percent
	 */
	public void aggregateQuadPercentage(Double percent) {
		if (percent != null) {
			this.sumOfPercentage += percent;
		}
	}
}

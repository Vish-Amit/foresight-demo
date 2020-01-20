package com.inn.foresight.module.nv.report.ib.utils.floorplandrawer.shapes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.inn.foresight.module.nv.report.ib.image.PointF;

public class ShapeUtils {
	public enum ThickNess {
		THIN, MEDIUM, THICK
	}

	public static List<Float> getAllX(List<PointF> vertices) {
		List<Float> tempList = new ArrayList<>();
		for (int i = 0; i < vertices.size(); i++) {
			try{
				tempList.add(vertices.get(i).getX().floatValue());
			}catch(NullPointerException e){
			}
		}
		return tempList;
	}

	public static List<Float> getAllY(List<PointF> vertices) {
		List<Float> tempList = new ArrayList<>();
		for (int i = 0; i < vertices.size(); i++) {
			try{
				tempList.add(vertices.get(i).getY().floatValue());
			}
			catch(NullPointerException e){
			}
		}
		return tempList;
	}

	public static int getWidthAsPerThickness(ThickNess thickNess) {
		if (!thickNess.toString().equalsIgnoreCase(ThickNess.THIN.toString())) {
			if (thickNess.toString().equalsIgnoreCase(ThickNess.THICK.toString())) {
				return 30;
			} else if (thickNess.toString().equalsIgnoreCase(ThickNess.MEDIUM.toString())) {
				return 15;
			}
		}
		return 10;
	}

	public static int getWidthAsPerThicknessForWindowInnerLines(ThickNess thickNess) {
		// if (thickNess.toString().equalsIgnoreCase(ThickNess.THIN.toString()))
		// {
		// return 10;
		// }
		// else if
		// (thickNess.toString().equalsIgnoreCase(ThickNess.THICK.toString())) {
		// return 30;
		// }
		// else if
		// (thickNess.toString().equalsIgnoreCase(ThickNess.MEDIUM.toString()))
		// {
		// return 15;
		// }
		return 2;
	}

	public static List<PointF> getPerpendicularToLineAtDistance(double x1, double y1, double x2, double y2, float N) {
		double dx = x1 - x2;
		double dy = y1 - y2;
		double dist = Math.sqrt(dx * dx + dy * dy);
		dx /= dist;
		dy /= dist;
		double x3 = x1 + (N / 2) * dy;
		double y3 = y1 - (N / 2) * dx;
		double x4 = x1 - (N / 2) * dy;
		double y4 = y1 + (N / 2) * dx;
		List<PointF> tempList = new ArrayList<>();
		tempList.add(new PointF(x3, y3));
		tempList.add(new PointF(x4, y4));
		return tempList;
	}

	public static List<PointF> getCircleLineIntersectionPoint(PointF pointA, PointF pointB, PointF center,
			float radius) {
		double baX = pointB.getX() - pointA.getX();
		double baY = pointB.getY() - pointA.getY();
		double caX = center.getX() - pointA.getX();
		double caY = center.getY() - pointA.getY();

		double a = baX * baX + baY * baY;
		double bBy2 = baX * caX + baY * caY;
		double c = caX * caX + caY * caY - radius * radius;

		double pBy2 = bBy2 / a;
		double q = c / a;

		double disc = pBy2 * pBy2 - q;
		if (disc < 0) {
			return Collections.emptyList();
		}
		// if disc == 0 ... dealt with later
		double tmpSqrt = Math.sqrt(disc);
		double abScalingFactor1 = -pBy2 + tmpSqrt;
		double abScalingFactor2 = -pBy2 - tmpSqrt;

		PointF p1 = new PointF(pointA.getX() - baX * abScalingFactor1, pointA.getY() - baY * abScalingFactor1);
		if (disc == 0) { // abScalingFactor1 == abScalingFactor2
			return Collections.singletonList(p1);
		}
		PointF p2 = new PointF(pointA.getX() - baX * abScalingFactor2, pointA.getY() - baY * abScalingFactor2);
		return Arrays.asList(p1, p2);
	}

	public static boolean isPointOnLine(PointF point, PointF firstPoint, PointF lastPoint) {
		PointF m = findProjectionOnLine(firstPoint, lastPoint, point);

		boolean lOnSegment = ((m.getX() >= firstPoint.getX() && m.getX() <= lastPoint.getX()) || (m.getX() >= lastPoint.getX() && m.getX() <= firstPoint.getX()))
				&& ((m.getY() >= firstPoint.getY() && m.getY() <= lastPoint.getY()) || (m.getY() >= lastPoint.getY() && m.getY() <= firstPoint.getY()));
		double distanceBetween = distanceBetween(m, point);
		return lOnSegment && distanceBetween < 20d;
	}

	public static PointF getCenterPointOfLine(PointF lFirstPoint, PointF lSecondPoint) {
		double x = (lFirstPoint.getX() + lSecondPoint.getX()) / 2;
		double y = (lFirstPoint.getY() + lSecondPoint.getY()) / 2;
		return new PointF(x, y);
	}

	public static boolean isPointOnLine(PointF point, PointF firstPoint, PointF lastPoint, double distance) {
		PointF m = findProjectionOnLine(firstPoint, lastPoint, point);
		boolean lOnSegment = ((m.getX() >= firstPoint.getX() && m.getX() <= lastPoint.getX()) || (m.getX() >= lastPoint.getX() && m.getX() <= firstPoint.getX()))
				&& ((m.getY() >= firstPoint.getY() && m.getY() <= lastPoint.getY()) || (m.getY() >= lastPoint.getY() && m.getY() <= firstPoint.getY()));
		double distanceBetween = distanceBetween(m, point);
		return lOnSegment && distanceBetween < distance;
	}

	public static PointF findProjectionOnLine(PointF p1, PointF p2, PointF my) {
		double x4, y4, k;
		k = ((p2.getY() - p1.getY()) * (my.getX() - p1.getX()) - (p2.getX() - p1.getX()) * (my.getY() - p1.getY()))
				/ ((float) Math.pow(p2.getY() - p1.getY(), 2) + (float) Math.pow(p2.getX() - p1.getX(), 2));
		x4 = my.getX() - k * (p2.getY() - p1.getY());
		y4 = my.getY() + k * (p2.getX() - p1.getX());
		return new PointF(x4, y4);
	}

	/**
	 * Takes two PointF points and return the distance between them.
	 *
	 * @param p1
	 * @param p2
	 */
	public static final double distanceBetween(PointF p1, PointF p2) {
		return distanceBetween(p1.getX(), p1.getY(), p2.getX(), p2.getY());
	}

	/**
	 * Takes x,y coordinates of two points and return the distance between them.
	 *
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public static final double distanceBetween(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.abs(x2 - x1) * Math.abs(x2 - x1) + Math.abs(y2 - y1) * Math.abs(y2 - y1));
	}

	public static float getAngleBetweenPoints(PointF pFirst, PointF pSecond) {
		double Rad2Deg = 180.0 / Math.PI;
		float angle = (float) (Math.atan2(pFirst.getY() - pSecond.getY(), pFirst.getX() - pSecond.getX()) * Rad2Deg);
		// Log.e("Math Utill angle1:", angle + "");
		angle = angle - 45;
		if (angle < 0) {
			angle = angle + 360;
		}
		// Log.e("Math Utill angle2:", angle + "");s
		return angle;
	}

	/**
	 * This method returns angle between two points from X-axis.
	 *
	 * @param pSource
	 * @param pTarget
	 * @return Angle between pSource and pTarget
	 */
	public static float calculateAngleBetweenPoints(PointF pSource, PointF pTarget) {
		 double Rad2Deg = 180.0 / Math.PI;
		return (float) (Math.atan2(pSource.getY() - pTarget.getY(), pSource.getX() - pTarget.getX()) * Rad2Deg);
	}
}

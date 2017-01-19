/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2009, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.mb.gpx;

import java.util.Collection;

import org.joda.time.DateTime;
import org.joda.time.Period;

public abstract class Waypoint {
	/**
	 * Returns the latitude of this SimpleWaypoint object. The latitude is in
	 * decimal degrees format.
	 */
	public abstract double getLatitude();

	/**
	 * Sets the latitude of this SimpleWaypoint object. The double value passed
	 * to this method should represent a valid latitude in decimal degrees
	 * format.
	 *
	 * @see org.geotools.gpx.utils.GpxUtils
	 */
	public abstract void setLatitude(double argLatitude);

	/**
	 * Returns the longitude of this SimpleWaypoint object. The longitude is in
	 * decimal degrees format.
	 */
	public abstract double getLongitude();

	/**
	 * Sets the longitude of this SimpleWaypoint object. The double value passed
	 * to this method should represent a valid longitude in decimal degrees
	 * format.
	 *
	 * @see org.geotools.gpx.utils.GpxUtils
	 */
	public abstract void setLongitude(double argLongitude);

	/**
	 * Returns the elevation of this SimpleWaypoint object. The elevation
	 * returned is usually in feet. However, this may change depending on the
	 * implementation of this interface.
	 */
	public abstract double getElevation();

	/**
	 * Sets the elevation of this SimpleWaypoint object. This value is typically
	 * in feet, but this may vary depending on the implementation of this
	 * interface. Negative elevation values are allowed and would represent
	 * elevations below sea level or some other datum.
	 */
	public abstract void setElevation(double argElevation);

	/**
	 * Returns the DateTime object that represents the date and time this this
	 * SimpleWaypoint object was collected. The calendar (time unit system) that
	 * this DateTime is referenced to will very depending on the implementation
	 * of this interface.
	 */
	public abstract DateTime getDateAndTimeCollected();

	/**
	 * Sets the DateTime object representing the date and time this
	 * SimpleWaypoint was collected.
	 */
	public abstract void setDateAndTimeCollected(DateTime argTime);

	/**
	 * Sets the name of this Waypoint. This name is not guaranteed to be unique.
	 */
	public abstract void setName(String argName);

	/**
	 * Returns the name of this Waypoint as a String.
	 */
	public abstract String getName();

	/**
	 * Indicates if this SimpleWaypoint object has a name.
	 */
	public abstract boolean hasName();

	/**
	 * Indicates if this SimpleWaypoint object has an elevation.
	 */
	public abstract boolean hasElevation();

	/**
	 * Indicates if this SimpleWaypoint has a DateTime object that reprensets
	 * the data and time it was collected.
	 */
	public abstract boolean hasDateAndTimeCollected();

	/**
	 * Gets the distance between two points.
	 *
	 * @param other
	 * @return
	 */
	public double distanceTo(Waypoint other) {
		return distFrom(getLatitude(), getLongitude(), other.getLatitude(), other.getLongitude());
	}

	public Period timeBetween(Waypoint other) {
		return new Period(this.getDateAndTimeCollected(), other.getDateAndTimeCollected());
	}

	public Waypoint getClosest(Collection<Waypoint> points) {
		Waypoint closest = null;
		double minDist = Double.MAX_VALUE;

		for (Waypoint point : points) {
			double dist = distanceTo(point);
			if (dist < minDist) {
				closest = point;
				minDist = dist;
			}
		}
		return closest;
	}

	public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
		double earthRadius = 3958.75;
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2) * Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double dist = earthRadius * c;

		return dist * 1.609344;
	}

	@Override
	public String toString() {
		StringBuilder res = new StringBuilder();
		if (hasName())
			res.append(getName());
		res.append(": " + getLatitude() + "," + getLongitude());
		if (hasElevation())
			res.append(", " + getElevation());
		return getName() + ": " + getLatitude() + "," + getLongitude();

		// String.format("wp %s: %d,%d", getName(), getLatitude(),
		// getLongitude());
	}

}

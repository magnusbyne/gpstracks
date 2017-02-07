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

import org.joda.time.DateTime;

/**
 * Provides a default implementation of the SimpleWaypoint interface. The double
 * values used to set the longitude and latitude values of a BasicWaypoint
 * object should be in decimal degrees. They must meet the requirements of the
 * tests in the GpxUtils class. BasicWaypoint objects are not required to have a
 * non-null elevation value, non-null DateTime value representing the date and
 * time the waypoint was collected, or a non-null name. In general it is
 * expected that BasicWaypoint objects created to represent a waypoint collected
 * with a GPS receiver will have non-null vales for these attributes.
 */
public class BasicWaypoint extends Waypoint {

	// Stores the latitude of the BasicWaypoint.
	private double latitude;

	// Stores the longitude of the BasicWaypoint.
	private double longitude;

	// Stores the elevation of the BasicWaypoint.
	private double elevation;

	// Stores the time the BasicWaypoint was collected.
	private DateTime time;

	// Stores the name of the BasicWaypoint. This is not necessarily
	// an integer or a unique identifier.
	private String name;

	// Indicates if this BasicWaypoint includes an elevation.
	private boolean hasElevation;

	// Indicates if this BasicWaypoint includes a collection data and time.
	private boolean hasTime;

	// Indicates if this BasicWaypoint includes a name.
	private boolean hasName;

	/**
	 * Constructs a BasicWaypoint object with the provided latitude and
	 * longitude.
	 *
	 * @param argLatitude
	 *            - The latitude for the BasicWaypoint object. This must be a
	 *            valid latitude value as tested by the GpxUtils class.
	 *
	 * @param argLongitude
	 *            The longitude for the BasicWaypoint object. This must be a
	 *            valid latitude value as tested by the GpxUtils class.
	 */
	public static BasicWaypoint makeWaypoint(double argLatitude, double argLongitude) throws IllegalArgumentException {
		// Latitude must be less than or equal to 90 and
		// greater than or equal to -90.
		// Longitude must be less than 180 and greater than
		// or equal to -180.

		boolean validLatitude = GpxUtils.isValidLatitudeValue(argLatitude);
		boolean validLongitude = GpxUtils.isValidLongitudeValue(argLongitude);

		if (validLatitude != true) {
			IllegalArgumentException toThrow1 = new IllegalArgumentException(
					"The latitude value passed to the BasicWaypoint constructor " + "was not valid.");

			throw toThrow1;
		}

		if (validLongitude != true) {
			IllegalArgumentException toThrow1 = new IllegalArgumentException(
					"The latitude value passed to the BasicWaypoint constructor " + "was not valid.");

			throw toThrow1;
		}

		BasicWaypoint toReturn = new BasicWaypoint();

		toReturn.latitude = argLatitude;
		toReturn.longitude = argLongitude;

		return toReturn;
	}

	/**
	 * Constructs a BasicWaypoint object with the provided latitude, longitude,
	 * and name.
	 *
	 * @param argLatitude
	 *            - The latitude for the BasicWaypoint object. This must be a
	 *            valid latitude value as tested by the GpxUtils class.
	 *
	 * @param argLongitude
	 *            The longitude for the BasicWaypoint object. This must be a
	 *            valid latitude value as tested by the GpxUtils class.
	 *
	 * @param argName
	 *            The name of the waypoint. This name may not be an integer or a
	 *            unique identifier. Any legal Java String could be a name.
	 * @return
	 */
	public static BasicWaypoint getBasicWaypoint(double argLatitude, double argLongitude, String argName) {
		BasicWaypoint toReturn = BasicWaypoint.makeWaypoint(argLatitude, argLongitude);

		toReturn.name = argName;
		toReturn.hasName = true;

		return toReturn;
	}

	/**
	 * Returns the latitude of this BasicWaypoint object as a double. This
	 * double is in decimal degrees format.
	 */
	@Override
	public double getLatitude() {
		return latitude;
	}

	/**
	 * Sets the latitude of the BasicWaypoint object. This must be a valid
	 * latitude value as tested by the GpxUtils class.
	 */
	@Override
	public void setLatitude(double argLatitude) {
		boolean isValid = GpxUtils.isValidLatitudeValue(argLatitude);

		if (isValid == true) {
			this.latitude = argLatitude;
		}

		else {
			IllegalArgumentException toThrow = new IllegalArgumentException(
					"The latitude value passed to the method was not valid.");

			throw toThrow;
		}
	}

	/**
	 * Returns the longitude of this BasicWaypoint object as a double. The
	 * double is in decimal degrees format.
	 */
	@Override
	public double getLongitude() {
		return longitude;
	}

	/**
	 * Sets the longitude of the BasicWaypoint object. This must be a valid
	 * longitude value as tested by the GpxUtils class.
	 */
	@Override
	public void setLongitude(double argLongitude) {
		boolean isValid = GpxUtils.isValidLongitudeValue(argLongitude);

		if (isValid == true) {
			this.longitude = argLongitude;
		}

		else {
			IllegalArgumentException toThrow = new IllegalArgumentException(
					"The longitude value passed to the method was not valid.");

			throw toThrow;
		}
	}

	/**
	 * Returns the elevation of this BasicWaypoint object. In most cases this
	 * will be a double representing the elevation above mean sea level in feet.
	 * This is not guaranteed and will depend on the GPS receiver or client code
	 * that created the BasicWaypoint object.
	 */
	@Override
	public double getElevation() {
		if (this.hasElevation == false) {
			IllegalStateException toThrow = new IllegalStateException(
					"The SimpleWaypoint does not have a valid " + "elevation property.");

			throw toThrow;
		}

		return this.elevation;
	}

	/**
	 * Sets the elevation of the BasicWaypoint object.
	 */
	@Override
	public void setElevation(double argElevation) {
		this.elevation = argElevation;
		this.hasElevation = true;
	}

	/**
	 * Returns the Joda DateTime object representing the date and time this
	 * waypoint object was collected by a GPS receiver.
	 */
	@Override
	public DateTime getDateAndTimeCollected() {
		if (this.hasTime == false) {

			return new DateTime();
			// IllegalStateException toThrow = new IllegalStateException(
			// "The SimpleWaypoint does not have a valid "
			// + "dateAndTimeCollected property.");

			// throw toThrow;
		}

		return time;
	}

	/**
	 * Sets the DateTime object representing the date and time this waypoint
	 * object was collected by a GPS receiver.
	 */
	@Override
	public void setDateAndTimeCollected(DateTime argTime) {
		this.time = argTime;
		this.hasTime = true;
	}

	/**
	 * Returns the name of this BasicWaypoint object. This can be any legal Java
	 * String object.
	 */
	@Override
	public String getName() {
		if (this.hasName == false) {
			IllegalStateException toThrow = new IllegalStateException(
					"The SimpleWaypoint does not have a valid " + "name property.");

			throw toThrow;
		}

		return this.name;
	}

	/**
	 * Sets the name of this BasicWaypoint object.
	 */
	@Override
	public void setName(String argName) {
		this.name = argName;
		this.hasName = true;
	}

	@Override
	public boolean hasDateAndTimeCollected() {
		return this.hasTime;
	}

	@Override
	public boolean hasElevation() {
		return this.hasElevation;
	}

	@Override
	public boolean hasName() {
		return this.hasName;
	}

}

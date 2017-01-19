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

import java.util.*;

/**
 * Provides a basic implementation of the TrackSegment interface.
 */
public class BasicTrackSegment implements TrackSegment {

	private LinkedList<Waypoint> waypoints = new LinkedList<Waypoint>();

	/**
	 * Constructs a BasicTrackSegment using the provided list of SimpleWaypoints
	 * representing the "track points" contained in in the TrackSegment.
	 */
	public BasicTrackSegment(List<Waypoint> argWaypoints) {
		this.waypoints.addAll(argWaypoints);
	}

	@Override
	public List<Waypoint> getTrackPoints() {
		return this.waypoints;
	}

	@Override
	public int getNumberOfTrackPoints() {
		return this.waypoints.size();
	}
}

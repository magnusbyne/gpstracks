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

import com.hs.gpxparser.modal.Waypoint;

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
public class WaypointHelper  {

	private Waypoint waypoint;

	WaypointHelper(Waypoint wp) {
		this.waypoint = wp;
	}
    /**
     * Gets the distance between two points.
     *
     * @param other
     * @return
     */
    public double distanceTo(Waypoint other) {
            return distFrom(waypoint.getLatitude(), waypoint.getLongitude(), other.getLatitude(), other.getLongitude());
    }

    public Period timeBetween(Waypoint other) {
            return new Period(new DateTime(waypoint.getTime()), new DateTime(other.getTime()));
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

    private static double distFrom(double lat1, double lng1, double lat2, double lng2) {
            double earthRadius = 3958.75;
            double dLat = Math.toRadians(lat2 - lat1);
            double dLng = Math.toRadians(lng2 - lng1);
            double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1))
                            * Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2) * Math.sin(dLng / 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            double dist = earthRadius * c;

            return dist * 1.609344;
    }


}

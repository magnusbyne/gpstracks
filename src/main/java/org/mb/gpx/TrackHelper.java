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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.joda.time.DateTime;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.hs.gpxparser.modal.Track;
import com.hs.gpxparser.modal.TrackSegment;
import com.hs.gpxparser.modal.Waypoint;

/**
 * Provides the default implementation of the Track interface.
 */
public class TrackHelper {
	private String name;
	private final Track track;

	/**
	 * Constructs a BasicTract from the list of TrackSegments provided as an
	 * argument.
	 */
	public TrackHelper(Track track) {
		this.track = track;
	}

	/**
	 * Constructs a BasicTrack from the list of TrackSegments provided as the
	 * first argument and with the name provided as the second argument.
	 */
	static public Track makeTrack(List<Waypoint> points,
			String argName) {
		
		TrackSegment ts = new TrackSegment();
		ts.setWaypoints(Lists.newArrayList(points));
		Track t = new Track();
		t.addTrackSegment(ts);
		t.setName(argName);
		return t;
	}

	public String getName() {
		return track.getName();
	}

	public void setName(String argName) {
		track.setName(argName);
	}

	/**
	 * Indicates if this BasicTrack has a valid name value.
	 */
	public boolean hasName() {
		return getName() != null;
	}

	public int getNumberOfSegments() {
		return track.getTrackSegments().size();
	}

	public List<TrackSegment> getTrackSegments() {
		return ImmutableList.copyOf(track.getTrackSegments());
	}

	public void addTrackSegment(TrackSegment argTrackSegment) {
		track.addTrackSegment(argTrackSegment);
	}

	public Waypoint getHighestPoint() {
		Waypoint highest = null;
		for (TrackSegment seg : getTrackSegments()) {
			for (Waypoint point : seg.getWaypoints()) {
				if (highest == null ||  point.getElevation() > highest.getElevation())
					highest = point;
			}
		}
		return highest;
	}

	public Waypoint getFirst() {
		return getTrackSegments().get(0).getWaypoints().get(0);
	}

	public DateTime getDate() {
		Waypoint first = getFirst();
		return new DateTime(first.getTime());
	}
}

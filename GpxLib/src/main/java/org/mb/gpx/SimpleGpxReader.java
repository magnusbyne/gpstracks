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

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.joda.time.DateTime;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.hs.gpxparser.GPXParser;
import com.hs.gpxparser.modal.GPX;
import com.hs.gpxparser.modal.Track;
import com.hs.gpxparser.modal.TrackSegment;
import com.hs.gpxparser.modal.Waypoint;

public class SimpleGpxReader {

	private final GPX gpx;

	private SimpleGpxReader(String filename) throws Exception {
		GPXParser parser = new GPXParser();
		gpx = parser.parseGPX(new FileInputStream(new File(filename)));
		int i=0;
		i++;
	}

	static public Set<Waypoint> getTrackPoints(String filename) {
		Set<Waypoint> wps= Sets.newHashSet();
		try {
			List<Track> tracks = new SimpleGpxReader(filename).getTracks();
			for (Track track : tracks)
				for(TrackSegment s : track.getTrackSegments())
					wps.addAll(s.getWaypoints());
			return wps;
		} catch (Exception e) {
			throw new IllegalStateException("Problem reading tracks from " + filename, e);
		}
	}
	
	
	static public List<Track> getTracks(String filename) {
		try {
			return new SimpleGpxReader(filename).getTracks();
		} catch (Exception e) {
			throw new IllegalStateException("Problem reading tracks from " + filename, e);
		}
	}

	static public List<Waypoint> getWaypoints(String filename) {
		try {
			return new SimpleGpxReader(filename).getWaypoints();
		} catch (Exception e) {
			System.err.println("Problem reading waypoitns: " + e);
			throw new RuntimeException(e);
		}
	}
	
	public List<Waypoint> getWaypoints() {
		return ImmutableList.copyOf(gpx.getWaypoints());
	}

	public List<Track> getTracks() {
		return ImmutableList.copyOf(gpx.getTracks());
	}

}

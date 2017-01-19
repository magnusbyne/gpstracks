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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.joda.time.DateTime;

public class SimpleGpxReader {

	private final Document targetDoc;

	private SimpleGpxReader(String filename) throws Exception {
		SAXBuilder builder = new SAXBuilder();
		targetDoc = builder.build(filename);
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

	@SuppressWarnings("unchecked")
	private List<Element> getChildren(Element node) {
		return node.getChildren();
	}

	public String getContent(Element node) {
		return node.getTextTrim();
	}

	public Waypoint parseWaypoint(Element argWaypointElement) {
		BasicWaypoint toReturn = new BasicWaypoint();

		String latitudeAsString = argWaypointElement.getAttributeValue("lat");
		double latitude = Double.parseDouble(latitudeAsString);

		toReturn.setLatitude(latitude);

		String longitudeAsString = argWaypointElement.getAttributeValue("lon");
		double longitude = Double.parseDouble(longitudeAsString);

		toReturn.setLongitude(longitude);

		for (Element currentElement : getChildren(argWaypointElement)) {

			String currentElementName = currentElement.getName();

			if (currentElementName.equals("ele")) {
				currentElement.getTextTrim();
				String elevationAsString = getContent(currentElement);
				double elevation = Double.parseDouble(elevationAsString);

				toReturn.setElevation(elevation);
			}

			if (currentElementName.equals("name")) {
				String nameAsString = getContent(currentElement);

				toReturn.setName(nameAsString);
			}

			if (currentElementName.equals("time")) {
				String timeAsString = getContent(currentElement);
				DateTime dateAndTimeCollected = new DateTime(timeAsString);
				toReturn.setDateAndTimeCollected(dateAndTimeCollected);
			}
		}

		return toReturn;
	}

	public BasicTrack parseTrack(Element argTrackElement) {
		BasicTrack toReturn = new BasicTrack();

		for (Element currentElement : getChildren(argTrackElement)) {

			String currentElementName = currentElement.getName();

			if (currentElementName.equals("name") == true) {
				String name = getContent(currentElement);
				toReturn.setName(name);
			}

			if (currentElementName.equals("trkseg") == true) {
				BasicTrackSegment trackSegment = this.parseTrackSegment(currentElement);

				toReturn.addTrackSegment(trackSegment);
			}
		}

		return toReturn;
	}

	public List<Waypoint> getWaypoints() {
		Element rootElement = targetDoc.getRootElement();
		List<Waypoint> waypoints = new ArrayList<Waypoint>();
		for (Element currentElement : getChildren(rootElement)) {

			// for (Element currentElement : getChildren(currentElement1)) {

			String currentElementName = currentElement.getName();

			if (currentElementName.equals("wpt") == true) {
				Waypoint toAdd = this.parseWaypoint(currentElement);
				waypoints.add(toAdd);
			}
			// }
		}

		return waypoints;
	}

	public List<Track> getTracks() {
		Element rootElement = targetDoc.getRootElement();

		LinkedList<Track> tracks = new LinkedList<Track>();

		for (Element currentElement : getChildren(rootElement)) {

			String currentElementName = currentElement.getName();

			if (currentElementName.equals("trk") == true) {
				Track toAdd = this.parseTrack(currentElement);
				tracks.add(toAdd);
			}
		}
		return tracks;
	}

	private BasicTrackSegment parseTrackSegment(Element argTrackSegmentElement) {

		List<Waypoint> trackPoints = new ArrayList<Waypoint>();
		for (Element currentElement : getChildren(argTrackSegmentElement)) {

			String currentElementName = currentElement.getName();

			if (currentElementName.equals("trkpt") == true) {
				Waypoint toAdd = this.parseWaypoint(currentElement);
				trackPoints.add(toAdd);
			}
		}

		BasicTrackSegment toReturn = new BasicTrackSegment(trackPoints);
		return toReturn;
	}

	/**
	 * Indicates if the target document being processed by this reader has
	 * waypoint elements.
	 */
	public boolean targetDocumentHasWaypoints() {
		Element rootElement = targetDoc.getRootElement();

		for (Element currentElement : getChildren(rootElement)) {
			String currentElementName = currentElement.getName();

			if (currentElementName.equals("wpt") == true) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Indicates if the target document being processed by this reader has track
	 * elements.
	 *
	 * @return
	 */
	public boolean targetDocumentHasTracks() {
		Element rootElement = targetDoc.getRootElement();

		for (Element currentElement : getChildren(rootElement)) {

			String currentElementName = currentElement.getName();

			if (currentElementName.equals("trk") == true) {
				return true;
			}
		}

		return false;
	}
}

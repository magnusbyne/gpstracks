package org.mb.gpx;

import java.io.FileOutputStream;
import java.util.Collection;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class SimpleGpxWriter {

	private static DateTimeFormatter format = DateTimeFormat
			.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

	public static void writeWaypoints(String filename,
			Collection<Waypoint> points) {
		Element gpx = new Element("gpx", defaultNS);
		Document myDocument = new Document(gpx);

		for (Waypoint wp : points) {
			Element p = new Element("wpt", defaultNS);
			p.setAttribute("lat", Double.toString(wp.getLatitude()));
			p.setAttribute("lon", Double.toString(wp.getLongitude()));

			if (wp.hasName()) {
				p.addContent(new Element("name", defaultNS).setText(wp
						.getName()));
			}
			if (wp.hasElevation()) {
				p.addContent(new Element("ele", defaultNS).setText(Double
						.toString(wp.getElevation())));
			}
			gpx.addContent(p);
		}

		try {
			XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
			FileOutputStream output = new FileOutputStream(filename);
			outputter.output(myDocument, output);
		} catch (java.io.IOException e) {
			throw new IllegalStateException(e);
		}
	}

	static Namespace defaultNS = Namespace
			.getNamespace("http://www.topografix.com/GPX/1/1");

	public static void writeTracks(String filename,
			Collection<? extends Track> tracks) {

		Namespace xsiNS = Namespace.getNamespace("xsi",
				"http://www.w3.org/2001/XMLSchema-instance");
		Element gpx = new Element("gpx", defaultNS);
		gpx.setNamespace(defaultNS);

		gpx.setAttribute("version", "1.1");
		gpx.setAttribute("creator", "mb");

		gpx.setAttribute(
				"schemaLocation",
				"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd",
				xsiNS);
		// ,
		// xsiNS);

		// gpx.setAttribute("xmlns", "http://www.topografix.com/GPX/1/1");

		Document myDocument = new Document(gpx);

		for (Track track : tracks)
			writeTrackNode(track, gpx);

		try {
			XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
			FileOutputStream output = new FileOutputStream(filename);
			outputter.output(myDocument, output);
		} catch (java.io.IOException e) {
			throw new IllegalStateException(e);
		}
	}

	public static void writeTrack(String filename, Track track) {

		Element gpx = new Element("gpx", defaultNS);
		Document myDocument = new Document(gpx);

		writeTrackNode(track, gpx);

		try {
			XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
			FileOutputStream output = new FileOutputStream(filename);
			outputter.output(myDocument, output);
		} catch (java.io.IOException e) {
			throw new IllegalStateException(e);
		}
	}

	private static void writeTrackNode(Track track, Element gpx) {
		Element t = new Element("trk", defaultNS);

		gpx.addContent(t);
		Element tname = new Element("name", defaultNS);
		tname.setText(track.getName());
		t.addContent(tname);

		for (TrackSegment segment : track.getTrackSegments()) {
			Element tseg = new Element("trkseg", defaultNS);
			for (Waypoint wp : segment.getTrackPoints()) {
				Element point = new Element("trkpt", defaultNS);
				point.setAttribute("lat", Double.toString(wp.getLatitude()));
				point.setAttribute("lon", Double.toString(wp.getLongitude()));

				if (wp.hasElevation()) {
					Element elev = new Element("ele", defaultNS);
					elev.setText(Double.toString(wp.getElevation()));
					point.addContent(elev);
				}
				if (wp.hasDateAndTimeCollected()) {
					Element elev = new Element("time", defaultNS);
					elev.setText(wp.getDateAndTimeCollected().toString(format));
					point.addContent(elev);
				}
				tseg.addContent(point);
			}
			t.addContent(tseg);
		}
	}

}

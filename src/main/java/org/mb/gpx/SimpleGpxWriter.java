package org.mb.gpx;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.hs.gpxparser.GPXWriter;
import com.hs.gpxparser.modal.GPX;
import com.hs.gpxparser.modal.Track;
import com.hs.gpxparser.modal.Waypoint;

public class SimpleGpxWriter {

	public static void writeWaypoints(String filename,
			Collection<Waypoint> points) throws Exception {
		GPXWriter w = new GPXWriter();
		GPX f = new GPX();
		for(Waypoint point : points)
		f.addWaypoint(point);
		try (FileOutputStream output = new FileOutputStream(new File(filename))) {
			w.writeGPX(f, output);
		}
	}

	public static void writeTracks(String filename,
			Collection<? extends Track> tracks) throws Exception {

		GPXWriter w = new GPXWriter();
		GPX f = new GPX();
		for(Track track : tracks)
		f.addTrack(track);
		try (FileOutputStream output = new FileOutputStream(new File(filename))) {
			w.writeGPX(f, output);
		}
	}

	public static void writeTrack(String filename, Track track) throws Exception {

		GPXWriter w = new GPXWriter();
		GPX f = new GPX();
		f.addTrack(track);
		try (FileOutputStream output = new FileOutputStream(new File(filename))) {
			w.writeGPX(f, output);
		}
	}

}

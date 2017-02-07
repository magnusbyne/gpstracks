package org.mb.gpx;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.google.common.collect.Lists;
import com.hs.gpxparser.modal.Track;
import com.hs.gpxparser.modal.TrackSegment;
import com.hs.gpxparser.modal.Waypoint;

public class process {

	/**
	 * @param args
	 * @throws Exception 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException, Exception {

		// SimpleGpxWriter.writeWaypoints("waypoints.gpx",
		// GmlReader.getPoints("e:/gps/bc_geoname.gml"));
		
		List<Waypoint> places = SimpleGpxReader.getWaypoints("waypoints.gpx");

		if (args.length != 3) {
			System.out.println("Usage: process dir regex out");
			System.exit(1);
		}

		File dir = new File(args[0]);

		File[] children = dir.listFiles();
		List<String> files = new ArrayList<String>();
		if (children == null) {
			throw new IllegalArgumentException(args[0]);
		} else {
			for (File child : children) {
				// Get filename of file or directory

				if (child.getName().matches(args[1]))
					files.add(child.getAbsolutePath());
			}
		}

		List<Track> tracks = new ArrayList<Track>();
		SortedSet<Waypoint> points = new TreeSet<Waypoint>(new Comparator<Waypoint>() {

			@Override
			public int compare(Waypoint o1, Waypoint o2) {
				return o1.getTime().compareTo(o2.getTime());
			}
		});

		for (String file : files) {
			points.addAll( SimpleGpxReader.getTrackPoints(file).stream().filter(point -> point.getTime() != null).collect(Collectors.toSet()));	
		}

		List<Track> output = new ArrayList<Track>();
		List<Waypoint> t = new ArrayList<Waypoint>();
		for (Waypoint point : points) {
			WaypointHelper wp = new WaypointHelper(point);
			if (t.isEmpty()) {
				t.add(point);
				continue;
			}
			Waypoint previous = t.get(t.size() - 1);
			double dist = wp.distanceTo(previous);
			Period period = wp.timeBetween(previous);

			if (dist > 3.0 || period.getHours() > 16) {
				// new segment
				if (t.size() > 50) {
					Track out = new Track();
					com.hs.gpxparser.modal.TrackSegment ts = new com.hs.gpxparser.modal.TrackSegment();
					ts.setWaypoints(Lists.newArrayList(clean(t)));
					out.addTrackSegment(ts);
					output.add(out);
				} else
					System.out.println("Ignoring short track");
				t.clear();

			}
			t.add(point);
		}

		addTrack(output, (t));

		for (Track trk : output) {
			TrackHelper helper = new TrackHelper(trk);
			Waypoint highest = helper.getHighestPoint();
			Waypoint cloesest = new WaypointHelper(highest).getClosest(places);
			System.out.println(cloesest.getName());
			trk.setName(cloesest.getName());

			SimpleGpxWriter.writeTrack(args[2] + "/" + helper.getDate().toString(format) + cloesest.getName() + ".gpx",
					trk);

		}

		System.out.println(output.size());

	}

	/**
	 * trim the first two points of the track as they are often very wrong
	 *
	 * @param points
	 * @return
	 */
	private static List<Waypoint> clean(List<Waypoint> points) {
		return points.subList(2, points.size());
	}

	private static DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd");

	private static void addTrack(List<Track> output, List<Waypoint> t) {
		if (t.size() > 50)
			output.add(TrackHelper.makeTrack(clean(t), ""));
	}


}

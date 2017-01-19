package org.mb.gpx;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class process {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

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
		for (String file : files) {
			tracks.addAll(SimpleGpxReader.getTracks(file));
		}
		SortedSet<Waypoint> points = allPoints(tracks);

		List<Track> output = new ArrayList<Track>();
		List<Waypoint> t = new ArrayList<Waypoint>();
		for (Waypoint point : points) {
			if (t.isEmpty()) {
				t.add(point);
				continue;
			}
			Waypoint previous = t.get(t.size() - 1);
			double dist = point.distanceTo(previous);
			Period period = point.timeBetween(previous);

			if (dist > 3.0 || period.getHours() > 16) {
				// new segment
				if (t.size() > 50)
					output.add(new BasicTrack(
							Collections.singletonList(new BasicTrackSegment(new ArrayList<Waypoint>(clean(t))))));
				else
					System.out.println("Ignoring short track");
				t.clear();

			}
			t.add(point);
		}

		addTrack(output, (t));

		for (Track trk : output) {
			Waypoint highest = trk.getHighestPoint();
			Waypoint cloesest = highest.getClosest(places);
			System.out.println(cloesest.getName());
			trk.setName(cloesest.getName());

			SimpleGpxWriter.writeTrack(args[2] + "/" + trk.getDate().toString(format) + cloesest.getName() + ".gpx",
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
			output.add(new BasicTrack(
					Collections.singletonList(new BasicTrackSegment(new ArrayList<Waypoint>(clean(t))))));
	}

	public static SortedSet<Waypoint> allPoints(Collection<Track> tracks) {
		SortedSet<Waypoint> points = new TreeSet<Waypoint>(new Comparator<Waypoint>() {

			@Override
			public int compare(Waypoint o1, Waypoint o2) {
				return o1.getDateAndTimeCollected().compareTo(o2.getDateAndTimeCollected());
			}
		});

		for (Track t : tracks) {
			for (TrackSegment s : t.getTrackSegments()) {
				points.addAll(s.getTrackPoints());
			}
		}
		return points;
	}

}

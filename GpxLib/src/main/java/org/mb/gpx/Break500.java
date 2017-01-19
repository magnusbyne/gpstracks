package org.mb.gpx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This is used to break longer tracks into short parts that can fit in
 * a single garmin track
 * @author bynem
 *
 */
public class Break500 {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.out.println("Usage: process gpxfile");
			System.exit(1);
		}

		List<Track> itracks = new ArrayList<Track>();

		itracks.addAll(SimpleGpxReader.getTracks(args[0]));

		List<Waypoint> points = new ArrayList<Waypoint>();
		for (Track t : itracks) {
			for (TrackSegment s : t.getTrackSegments()) {
				points.addAll(s.getTrackPoints());
			}
		}

		List<BasicTrack> tracks = new ArrayList<BasicTrack>();

		int start = 0;
		while (points.size() > start) {
			tracks.add(BasicTrack.makeTrack(points.subList(start, Math.min(points.size(), start + 500)), getName()));
			start += 500;
		}

		SimpleGpxWriter.writeTracks("e:/output500.gpx", tracks);

		System.out.println("Wrote Tracks:" + tracks.size());

	}

	static int count = 0;

	static String getName() {
		return "north" + count++;
	}

}

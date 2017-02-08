package org.mb.gpx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.hs.gpxparser.modal.Track;
import com.hs.gpxparser.modal.TrackSegment;
import com.hs.gpxparser.modal.Waypoint;

/**
 * This is used to break longer tracks into short parts that can fit in
 * a single garmin track
 * @author bynem
 *
 */
public class Break500 {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.out.println("Usage: process gpxfile");
			System.exit(1);
		}

		List<Track> itracks = new ArrayList<Track>();

		itracks.addAll(SimpleGpxReader.getTracks(args[0]));

		List<Waypoint> points = new ArrayList<Waypoint>();
		for (Track t : itracks) {
			for (TrackSegment s : t.getTrackSegments()) {
				points.addAll(s.getWaypoints());
			}
		}

		List<Track> tracks = new ArrayList<Track>();

		int start = 0;
		while (points.size() > start) {
			tracks.add(TrackHelper.makeTrack(points.subList(start, Math.min(points.size(), start + 500)), getName()));
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

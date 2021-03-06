package org.mb.gpx;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class convert {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.out.println("Usage: process points_file");
			System.exit(1);
		}

		String input = readFileAsString(args[0]);

		Pattern p = Pattern
				.compile("\\{(-?[0-9]+\\.[0-9]*),\\s+(-?[0-9]+\\.[0-9]+)\\},?\\s*");
		Matcher matcher = p.matcher(input);

		List<Waypoint> points = new ArrayList<Waypoint>();
		while (matcher.find()) {
			points.add(BasicWaypoint.makeWaypoint(
					Double.parseDouble(matcher.group(2)),
					Double.parseDouble(matcher.group(1))));
		}

		List<BasicTrack> tracks = new ArrayList<BasicTrack>();

		int start = 0;
		while (points.size() > start) {
			tracks.add(BasicTrack.makeTrack(
					points.subList(start, Math.min(points.size(), start + 500)),
					getName()));
			start += 500;
		}

		SimpleGpxWriter.writeTracks("e:/output.gpx", tracks);

		System.out.println("Wrote Tracks:" + tracks.size());

	}

	static int count = 0;

	static String getName() {
		return "north" + count++;
	}

	private static String readFileAsString(String filePath)
			throws java.io.IOException {
		StringBuffer fileData = new StringBuffer(1000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		char[] buf = new char[1024];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}
		reader.close();
		return fileData.toString();
	}
}

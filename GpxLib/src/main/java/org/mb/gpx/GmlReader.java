package org.mb.gpx;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;

public class GmlReader {
	private final Document targetDoc;

	private GmlReader(String filename) throws Exception {
		SAXBuilder builder = new SAXBuilder();
		targetDoc = builder.build(filename);
	}

	static public List<Waypoint> getPoints(String filename) {
		try {
			return new GmlReader(filename).getPoints();
		} catch (Exception e) {
			System.err.println("Problem reading tracks: " + e);
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	private List<Element> getChildren(Element node) {
		return node.getChildren();
	}

	private static Namespace nscgn = Namespace.getNamespace("http://www.geobase.ca/cgn");

	private static Namespace nsgml = Namespace.getNamespace("http://www.opengis.net/gml");

	private List<Waypoint> getPoints() {
		List<Waypoint> res = new ArrayList<Waypoint>();
		for (Element e : getChildren(targetDoc.getRootElement())) {
			if (e.getName().equals("featureMember")) {

				Element geoname = e.getChild("geoname", nscgn);
				if (geoname != null) {
					String name = geoname.getChild("GEONAME", nscgn).getText();

					Element pos = geoname.getChild("pointProperty", nsgml).getChild("Point", nsgml).getChild("pos",
							nsgml);

					String[] p = pos.getTextTrim().split(" ");

					Waypoint wp = BasicWaypoint.getBasicWaypoint(Double.parseDouble(p[0]), Double.parseDouble(p[1]),
							name);
					res.add(wp);
				}
			}
		}
		return res;
	}

}

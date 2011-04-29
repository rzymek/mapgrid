package pl.mapgrid.utils;

import java.awt.image.RenderedImage;
import java.io.File;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.xml.sax.InputSource;

import pl.mapgrid.calibration.coordinates.Coordinates;

public class Utils {

	public static File basename(File f, String ext) {
		String path = f.getPath();
		String basename = path.substring(0, path.lastIndexOf('.'));
		String filename = basename + ext;
		return new File(filename);
	}

	public static String xpath(InputSource source, String xpath) {
		try {
			XPathFactory factory = XPathFactory.newInstance();
			XPath xp = factory.newXPath();
			XPathExpression expr = xp.compile(xpath);
			return expr.evaluate(source);
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	public static enum CoordType {LAT,LON,X,Y};
	public static double get(Coordinates c, CoordType type) {
		switch (type) {
		case LAT: return c.getLat();
		case LON: return c.getLon();
		case X:   return c.getX();
		case Y:   return c.getY();
		default:  throw new AssertionError("Consitency error:"+type);
		}
	}
	
	public static enum DimentionType {WIDTH, HEIGHT};
	public static int getDimention(RenderedImage image, DimentionType type) {
		switch (type) {
		case HEIGHT: return image.getHeight();
		case WIDTH:  return image.getWidth();
		default:     throw new AssertionError("Consitency error:"+type);		
		}
	}

}

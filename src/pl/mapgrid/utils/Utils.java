package pl.mapgrid.utils;

import java.io.File;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.xml.sax.InputSource;

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
}

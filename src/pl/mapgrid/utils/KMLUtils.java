package pl.mapgrid.utils;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.micromata.opengis.kml.v_2_2_0.ColorStyle;
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.LabelStyle;
import de.micromata.opengis.kml.v_2_2_0.LineStyle;
import de.micromata.opengis.kml.v_2_2_0.Pair;
import de.micromata.opengis.kml.v_2_2_0.Style;
import de.micromata.opengis.kml.v_2_2_0.StyleMap;
import de.micromata.opengis.kml.v_2_2_0.StyleSelector;
import de.micromata.opengis.kml.v_2_2_0.StyleState;

public class KMLUtils {

	public static Map<String, Style> createStyleMap(Kml kml) {
		Document doc = (Document) kml.getFeature();
		Map<String, Style> styleMap = new HashMap<String, Style>();
		Map<String, String> tmpMap = new HashMap<String, String>();

		List<StyleSelector> styles = doc.getStyleSelector();
		for (StyleSelector selector : styles) {
			if (selector instanceof Style) {
				Style s = (Style) selector;
				styleMap.put("#"+selector.getId(), s);
			} else if (selector instanceof StyleMap) {
				StyleMap s = (StyleMap) selector;
				String url = getStyleUrl(s);
				Style style = styleMap.get(url);
				if(style == null)
					tmpMap.put("#"+selector.getId(), url);
				else
					styleMap.put("#"+selector.getId(), style);
			}
		}
		Set<Entry<String, String>> entrySet = tmpMap.entrySet();
		for (Entry<String, String> entry : entrySet) {
			String key = entry.getKey();
			Style style = styleMap.get(entry.getValue());
//			if(style != null)
				styleMap.put(key, style);
		}
//		for (Entry<String, Style> entry : styleMap.entrySet()) {
//			System.out.println(entry);
//		}
		return styleMap;
	}

	private static String getStyleUrl(StyleMap s) {
		List<Pair> pairs = s.getPair();
		for (Pair pair : pairs) {
			if (StyleState.NORMAL == pair.getKey())
				return pair.getStyleUrl();
		}
		return null;
	}

	public static Color getColor(Style style) {
		ColorStyle s;
		s = style.getLineStyle();
		if(s == null)
			s = style.getPolyStyle();
		if(s == null)
			s = style.getLabelStyle();
		if(s == null)
			s = style.getIconStyle();
		if(s == null)
			return null;
		String color = s.getColor();
		if(color == null)
			return null;
		return decode(color);
	}

	private static Color decode(String color) {
		/** http://code.google.com/apis/kml/documentation/kmlreference.html#colorstyle
		 * Color and opacity (alpha) values are expressed in hexadecimal notation. 
		 * The range of values for any one color is 0 to 255 (00 to ff). 
		 * For alpha, 00 is fully transparent and ff is fully opaque. 
		 * The order of expression is aabbggrr, where aa=alpha (00 to ff); bb=blue (00 to ff); 
		 * gg=green (00 to ff); rr=red (00 to ff). 
		 * For example, if you want to apply a blue color with 50 percent opacity to an overlay, 
		 * you would specify the following: <color>7fff0000</color>, where 
		 * alpha=0x7f, blue=0xff, green=0x00, and red=0x00.
		 */
		long i = Long.parseLong(color, 16);
		int r = (int) (i & 0xff);
		int g = (int) ((i >> 8) & 0xff);
		int b = (int) ((i >> 16) & 0xff);
		int a = (int) ((i >> 24) & 0xff);
		return new Color(r, g, b, a);
	}

	public static double getWidth(Style style) {
		LineStyle lineStyle = style.getLineStyle();
		if(lineStyle == null) {
			LabelStyle labelStyle = style.getLabelStyle();
			if(labelStyle == null)
				return 0.0;
			return labelStyle.getScale();
		}
		return lineStyle.getWidth();
	}

}

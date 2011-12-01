package pl.maps.tms.gui;

import java.awt.BorderLayout;
import java.text.DecimalFormatSymbols;

import javax.swing.JLabel;
import javax.swing.JPanel;

import pl.mapgrid.calibration.coordinates.Coordinates;
import pl.mapgrid.calibration.coordinates.LatLon;
import pl.mapgrid.calibration.coordinates.PUWG92;
import pl.mapgrid.calibration.coordinates.UTM;
import pl.mapgrid.mask.Doc;

public class JOptionsPanel extends JPanel {
	public static class Config {
		@Doc("Export zoom")
		public int zoom;
	}
	private JLabel bottomInfo = new JLabel();
	public JOptionsPanel() {
		super(new BorderLayout());
		add(bottomInfo, BorderLayout.SOUTH);
	}
	
	public void setLocation(Coordinates c){
		String text = toHTML(c);
		bottomInfo.setText(text);
	}

	private static String toHTML(Coordinates c) {
		PUWG92 puwg92 = new PUWG92(c);
		UTM utm = new UTM(c);
		LatLon latLon = new LatLon(c);
		
		String slatLon = String.format("N %02.08f\u00B0 E %02.08f\u00B0", latLon.getLat(), latLon.getLon());
		String slatLonDeg = GeoUtils.getDegMinFormat(latLon.getLat(), true) + " " + GeoUtils.getDegMinFormat(latLon.getLon(), false);
		
		CoordinateDms2Format fmt = new CoordinateDms2Format(new DecimalFormatSymbols());
		String slatLonSec = "N "+fmt.format(latLon.getLat())+" E "+fmt.format(latLon.getLon());
		String sutm= String.format("%s %.0f E %.0f N", utm.getZone(), utm.getEasting(), utm.getNorthing());
		String spuwg = String.format("X %06.0f   Y %06.0f", puwg92.getY(), puwg92.getX()); 
		String text = "<html>\n"+
//			slatLon+"<br/>\n"+
//			slatLonDeg+"<br/>\n"+
//			slatLonSec+"<br/>\n"+
			spuwg+"<br/>\n"+
//			sutm+
			"\n</html>";
		return text;
	}
	
	public static void main(String[] args) {
		LatLon c = new LatLon(52.19413, 21.05139);
		String s = toHTML(c);
		System.out.println(s);
	}
}

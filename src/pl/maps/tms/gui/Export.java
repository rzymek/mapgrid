package pl.maps.tms.gui;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

import javax.imageio.ImageIO;

import pl.mapgrid.calibration.coordinates.Coordinates;
import pl.maps.tms.HTTPTileImageProvider;
import pl.maps.tms.View;
import pl.maps.tms.cache.AsyncTileCache;
import pl.maps.tms.providers.GeoportalTileProvider;

public class Export {
	private final AsyncTileCache cache;
	private final File file;
	
	public Export(AsyncTileCache cache, File file) {
		this.cache = cache;
		this.file = file;
	}
	
	public void export(Coordinates loc1, Coordinates loc2, View view) {
		try {
			Dimension s = view.getViewSize();
			
			BufferedImage img = new BufferedImage(s.width, s.height, BufferedImage.TYPE_3BYTE_BGR);
			view.paint(img.getGraphics());
			
			ImageIO.write(img, "jpg", file);
			String baseName = getBaseName(file);
			writeCalib(view, s.width, s.height, baseName);
			writeTFWCalib(loc1, loc2, s.width, s.height, baseName+".tfw");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	private static String getBaseName(File file) {
		String path = file.getPath();
		return path.substring(0, path.lastIndexOf('.'));
	}

	public View createView(Coordinates loc1, Coordinates loc2, int zoom) {
		GeoportalTileProvider grid = new GeoportalTileProvider();
		HTTPTileImageProvider http = new HTTPTileImageProvider(grid);
		ChainImageProvider images = new ChainImageProvider(cache, http); 

		View view = new View(new Dimension(1, 1), grid, images);			
		view.setZoom(zoom);
		view.setLeftTop(loc1);
		Point end= view.getPoint(loc2);
		int w = end.x;
		int h = end.y;
		view.setDimension(new Dimension(w, h));
		return view;
	}
	
	private void writeTFWCalib(Coordinates c1, Coordinates c2, int w, int h, String filename) throws IOException {
		FileWriter out = new FileWriter(filename);
		try {
			double cw = Math.abs(c2.getX() - c1.getX());
			out.append(""+(cw/w)).append('\n');
			out.append("0.0\n");
			out.append("0.0\n");
			double ch = Math.abs(c2.getY() - c1.getY());
			out.append(""+(-ch/h)).append('\n');
			out.append(""+c1.getX()).append('\n');
			out.append(""+c1.getY()).append('\n');
			out.append("\n");
		}finally{
			out.close();
		}
	}
	
	private void writeCalib(View v, int width, int height, String filename) throws IOException {
		FileWriter out = new FileWriter(filename+".map");
		try {
			StringBuffer map = createOziCalib(v, width, height, filename);
			out.write(map.toString());
		}finally{
			out.close();
		}
	}

	private StringBuffer createOziCalib(View v,	int width, int height, String file) {
		String filename = file+".jpg";
		StringBuffer sbMap = new StringBuffer();

		sbMap.append("OziExplorer Map Data File Version 2.2\r\n");
		sbMap.append(filename + "\r\n");
		sbMap.append(filename + "\r\n");
		sbMap.append("1 ,Map Code,\r\n");
		sbMap.append("WGS 84,WGS 84,   0.0000,   0.0000,WGS 84\r\n");
		sbMap.append("Reserved 1\r\n");
		sbMap.append("Reserved 2\r\n");
		sbMap.append("Magnetic Variation,,,E\r\n");
		sbMap.append("Map Projection,Mercator,PolyCal,No," + "AutoCalOnly,No,BSBUseWPX,No\r\n");


		String pointLine = "Point%02d,xy, %4s, %4s,in, deg, %1s, %1s, grid, , , ,N\r\n";
		
		int[] x = {0,width-1,width-1,0};
		int[] y = {0,0,height-1,height-1};
		Coordinates[] c = new Coordinates[4];
		for (int i = 0; i < c.length; i++) {
			c[i] = v.getCoordinates(x[i],y[i]);
		}
		for (int i = 0; i < c.length; i++) {
			String lat = GeoUtils.getDegMinFormat(c[i].getLat(), true);
			String lon = GeoUtils.getDegMinFormat(c[i].getLon(), false);
			sbMap.append(String.format(pointLine, i+1, x[i], y[i], lat, lon));
		}
		for (int i = 5; i <= 30; i++) {
			String s = String.format(pointLine, i, "", "", "", "");
			sbMap.append(s);
		}
		sbMap.append("Projection Setup,,,,,,,,,,\r\n");
		sbMap.append("Map Feature = MF ; Map Comment = MC     These follow if they exist\r\n");
		sbMap.append("Track File = TF      These follow if they exist\r\n");
		sbMap.append("Moving Map Parameters = MM?    These follow if they exist\r\n");

		sbMap.append("MM0,Yes\r\n");
		sbMap.append("MMPNUM,4\r\n");

		String mmpxLine = "MMPXY, %d, %5d, %5d\r\n";

		sbMap.append(String.format(mmpxLine, 1, 0, 0));
		sbMap.append(String.format(mmpxLine, 2, width - 1, 0));
		sbMap.append(String.format(mmpxLine, 3, width - 1, height - 1));
		sbMap.append(String.format(mmpxLine, 4, 0, height - 1));

		String mpllLine = "MMPLL, %d, %2.6f, %2.6f\r\n";

		for(int i=0;i<c.length;++i) {
			sbMap.append(String.format(Locale.ENGLISH, mpllLine, i+1, c[i].getLon(), c[i].getLat()));
		}
		sbMap.append("MOP,Map Open Position,0,0\r\n");

		// The simple variant for calculating mm1b
		// http://www.trekbuddy.net/forum/viewtopic.php?t=3755&postdays=0&postorder=asc&start=286
//		double mm1b = (c[2].getLon() - c[0].getLon()) * 111319;
//		mm1b *= Math.cos(Math.toRadians((c[2].getLat() + c[0].getLat()) / 2.0)) / width;

//		sbMap.append(String.format(Locale.ENGLISH, "MM1B, %2.6f\r\n", mm1b));

		sbMap.append("IWH,Map Image Width/Height, " + width + ", " + height + "\r\n");
		return sbMap;
	}

}

package pl.mapgrid.grid;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.io.Serializable;
import java.util.List;

import pl.mapgrid.mask.Doc;

public abstract class GridGraphics {
	private final Graphics2D g;
	protected final Config config;
	private final int width;
	private final int height;
	
	public static class Config implements Serializable {
		@Doc("UTM: kolor lini")
		public Color[] lineColors = {
			new Color(0,0,0,128),
		};
		@Doc("UTM: grubość lini")
		public int lineSize = 3;
		@Doc("UTM: grubość ramki")
		public int border=40;
		@Doc("UTM: styl czcionki (1=bold, 2=italic)")
		public int fontStyle = Font.BOLD;
		@Doc("UTM: font")
		public String font = Font.MONOSPACED;
				
		@Doc("UTM: kolor 1 ramki ")
		public Color[] borderColors = {
			new Color(0,0,0),
			new Color(255,255,255),
		};		
		@Doc("Rozmiar kratki w metrach")
		public int step=1000;
		@Doc("Formatowanie wspołrzędnych: regex")
		public String labelSplit = "M(...) (.*) (..)(...)";//UTM: "(.*)(.)(...)";
		@Doc("Formatowanie wspołrzędnych: replacement")
		public String labelReplace = "$2 $3 $4";
	}
	
	public GridGraphics(Graphics2D g, Config config, int width, int height) {
		this.g = g;
		this.config = config;
		this.width = width;
		this.height = height;
	}
	
	public void drawGrid(List<int[]> vertical, List<int[]> horizontal, double cellWidth, double cellHeight) {
		g.setFont(new Font(Font.MONOSPACED, Font.BOLD, config.border));
		boolean HORIZONTAL = true;
		boolean VERTICAL  = false;
//		drawGrid1(vertical);
		drawGrid(vertical, horizontal, 1, cellWidth);
		drawGrid(horizontal, vertical, 0, cellHeight);
		g.setStroke(new BasicStroke());
		drawBoxes(vertical, VERTICAL);
		drawBoxes(horizontal, HORIZONTAL);
	}

	private void drawGrid(List<int[]> lines, List<int[]> other, int idx, double cellSize) {
		int start = other.get(0)[idx];
//		int w = other.get(2)[idx] - other.get(1)[idx];
		drawDashed(lines, start, 0, (int) cellSize);
		drawDashed(lines, start, 1, (int) cellSize);
	}

	private void drawDashed(List<int[]> lines, int start, int colorIdx, int w) {
		float step = Math.abs(w)/10.f;
		float[] dashPattern = new float[] {
				step,
				step,
		};
		g.setColor(config.lineColors[colorIdx % config.lineColors.length]);
		g.setStroke(new BasicStroke(
				 config.lineSize, 			// Width
				 BasicStroke.CAP_BUTT,    // End cap
                 BasicStroke.JOIN_BEVEL,    // Join style
                 10.0f,                     // Miter limit
                 dashPattern, // Dash pattern
                 (colorIdx%2)*step+(w-start)
		));
		for (int[] line : lines) 
			g.drawLine(line[0] , line[1], line[2], line[3]);
	}
	
	private void drawText(Graphics2D g, String s, int[] x, int[] y, int rotate) {
		FontMetrics fm = g.getFontMetrics();
		int h = fm.getAscent();
		int w = fm.stringWidth(s);
		int xx = x[0] + (x[1]-x[0])/2;
		int yy = y[0] + (y[3]-y[0])/2-1;
		AffineTransform save = g.getTransform();
		AffineTransform tx = new AffineTransform();
		tx.translate(xx, yy);		
		tx.rotate(Math.toRadians(rotate));
		g.setTransform(tx);
		g.drawString(s, -w/2, h/2);
		g.setTransform(save);
	}
	
	private void drawBoxes(List<int[]> lines, boolean horizontal) {
		// 0 ------ 1
		// | ...... |
		// 3--------2
		int[] x = new int[4];
		int[] y = new int[4];
		int borderColorIndex = 0;
		for (int i = -1; i < lines.size(); ++i) {
			int x1, y1, x2, y2;
			if (i == -1) {
				x1 = 0;
				y1 = 0;
				x2 = horizontal ? width : 0;
				y2 = horizontal ? 0 : height;
			} else {
				x1 = lines.get(i)[0];
				y1 = lines.get(i)[1];
				x2 = lines.get(i)[2];
				y2 = lines.get(i)[3];
			}
			int dx = x2 - x1;
			int dy = y2 - y1;
			int n1, n2;
			if (i + 1 >= lines.size()) {
				n1 = horizontal ? height : width; 
				n2 = horizontal ? height : width; 
			} else {
				int idx = horizontal ? 1 : 0;
				n1 = lines.get(i + 1)[idx]; 
				n2 = lines.get(i + 1)[idx+2];
			}
			int border = config.border;
			int b = horizontal ? (border * dy / dx) : (border * dx / dy);

			String label = getLabel(horizontal, i);
			if (i < 0 || i+1 >= lines.size())
				label = null;
			int rotation = horizontal ? 270 : 0;
			
			if(horizontal) {
				//upper
				arrayCopy(x, x1, x1 + border, x1 + border, x1);// !
				arrayCopy(y, y1, y1 + b, n1 + b, n1);// !
			}else{
				//left
				arrayCopy(x, x1, n1, n1+b,		x1+b);
				arrayCopy(y, y1, y1, y1+border,	y1+border);
			}
			drawSingleBorderBox(x, y, borderColorIndex, label, rotation);

			if(horizontal) {
				//lower
				arrayCopy(x, x2 - border, x2, x2, x2 - border);
				arrayCopy(y, y2 - b, y2, n2 - b, n2);
			}else{
				//right
				arrayCopy(x, x2 - b,      n2 - b,      n2, x2);
				arrayCopy(y, y2 - border, y2 - border, y2, y2);
			}
			drawSingleBorderBox(x, y, borderColorIndex, label, rotation);
			borderColorIndex = (borderColorIndex+1)%config.borderColors.length;
		}
	}

	protected abstract String getLabel(boolean horizontal, int i);

	private void drawSingleBorderBox(int[] x, int[] y, int borderColorIndex, String label, int rotation) {
		g.setColor(config.borderColors[borderColorIndex]);
		g.fillPolygon(x, y, x.length);
		
		borderColorIndex = (borderColorIndex+1)%config.borderColors.length;
		g.setColor(config.borderColors[0]);
		g.drawPolygon(x, y, x.length);
		if(label!=null) {
			g.setColor(config.borderColors[borderColorIndex]);
			drawText(g, label, x ,y, rotation);
		}
	}

	private void arrayCopy(int[] dst, int... src) {
		for (int i = 0; i < src.length; i++) 
			dst[i] = src[i];
	}
	
}

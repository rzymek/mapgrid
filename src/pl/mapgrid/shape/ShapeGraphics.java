package pl.mapgrid.shape;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import pl.mapgrid.mask.Doc;

public class ShapeGraphics {
	public static class Config implements Serializable {
		@Doc("Wielkość tekstu")
		public int fontSize = 20;
		@Doc("Kolor tekstu")
		public Color fontColor = Color.BLACK;
		@Doc("Kolor obramowania tekstu")
		public Color fontBorder = Color.WHITE;
		@Doc("Promień waypointów")
		public int baseWaypointRadius = 5;
	}
	private final Graphics2D g;
	private final Config config;

	public ShapeGraphics(Graphics2D g, Config config) {
		this.g = g;
		this.config = config;
	}
	
	public void draw(Shape<Point> s) {
		if(s.count() <= 0)
			return;
		if(s.style != null) {
			Color color = s.style.color;
			if(color != null)
				g.setColor(color);
			g.setStroke(new BasicStroke((float) s.style.width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		}
		if(s.count() == 1) {
			Point p = s.get(0);
			int r = config.baseWaypointRadius;
			g.fillOval(p.x-r, p.y-r, 2*r, 2*r);
			if(s instanceof Waypoint) {
				Waypoint<?> wp = (Waypoint<?>) s;
				String label = wp.label;
				if(label != null) 
					drawString(p, label, s.style , r);
			}
		} else {
			int[] x = new int[s.count()];
			int[] y = new int[s.count()];
			for(int i=0;i<s.count();++i) {
				Point p = s.get(i);
				x[i] = p.x;
				y[i] = p.y;
			}
			g.drawPolygon(x, y, s.count());
		}
	}

	private void drawString(Point p, String label, Style style, int r) {
		int fontSize = config.fontSize;
		if(style!=null)
			fontSize *= style.width;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
		FontRenderContext frc = g.getFontRenderContext();
		Font f = new Font(Font.MONOSPACED, Font.BOLD, fontSize);
		TextLayout tl = new TextLayout(label, f, frc);
		AffineTransform transform = new AffineTransform();
		Rectangle2D b = tl.getBounds();
		transform.setToTranslation(p.x - b.getWidth()/2,p. y - r);
		java.awt.Shape s = tl.getOutline(transform);
		g.setColor(config.fontColor);		
		g.fill(s);
		g.setColor(config.fontBorder);
		g.draw(s);
	}
}

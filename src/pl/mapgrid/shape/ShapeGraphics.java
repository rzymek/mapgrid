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

public class ShapeGraphics {
	private static final Color FONT_COLOR = Color.BLACK;
	private static int BASE_FONT_SIZE=25;
	private static int BASE_WAYPOINT_RADIUS=10;
	private final Graphics2D g;

	public ShapeGraphics(Graphics2D g) {
		this.g = g;
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
			int r = BASE_WAYPOINT_RADIUS;
			g.fillOval(p.x-r, p.y-r, 2*r, 2*r);
			if(s instanceof Waypoint) {
				Waypoint<?> wp = (Waypoint<?>) s;
				String label = wp.label;
				if(label != null) 
					drawString(p, label);
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

	private void drawString(Point p, String label) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(FONT_COLOR);		
		g.drawString(label, p.x, p.y);

		FontRenderContext frc = g.getFontRenderContext();
		Font f = new Font(Font.MONOSPACED, Font.BOLD, BASE_FONT_SIZE);
		TextLayout tl = new TextLayout(label, f, frc);
		AffineTransform transform = new AffineTransform();
		transform.setToTranslation(p.x,p.y);
		java.awt.Shape s = tl.getOutline(transform);
		g.setColor(FONT_COLOR);		
		g.fill(s);
		g.setColor(Color.WHITE);
		g.draw(s);
	}
}

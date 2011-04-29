package pl.mapgrid.shape;

import java.awt.Graphics;
import java.awt.Point;

public class ShapeGraphics {
	private final Graphics g;

	public ShapeGraphics(Graphics g) {
		this.g = g;
	}
	public void draw(Shape<Point> s) {
		if(s.count() <= 0)
			return;
		Point a = s.get(0);
		Point b;
		for(int i=1;i<s.count();++i) {
			b = s.get(i);
			g.drawLine(a.x, a.y, b.x, b.y);
			a = b;
		}
	}
}

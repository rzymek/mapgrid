package pl.maps.tms;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;

import pl.mapgrid.calibration.coordinates.Coordinates;

public class JTileMapView extends JComponent {
	public Coordinates center;
	private TileProvider tileProvider = new TileProvider();
	
	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0,0,getWidth(), getHeight());

		Dimension tileSize = tileProvider.getTileDimentions();
//		Point tile = tileProvider.getTile(center);
		
		int sx = (getWidth() / 2) % tileSize.width - tileSize.width;
		int sy = (getHeight() / 2) % tileSize.height - tileSize.height;
		g.setColor(Color.BLACK);
		int x = sx;
		int y = sy;
		for(;;) {
			if(isOutside(y, tileSize.height, getHeight()))
				break;
			for(;;) {
				if(isOutside(x, tileSize.width, getWidth()))
					break;
				g.drawRect(x,y,tileSize.width, tileSize.height);
				g.drawString(x+", "+y, x+tileSize.width/2, y+tileSize.height/2);
				x += tileSize.width;
			}
			y += tileSize.height;
			x = sx;
		}
		g.setColor(Color.RED);
		drawCrossHair(g, getWidth()/2, getHeight()/2);
	}

	private boolean isOutside(int x, int tileSize, int windowSize) {
		return (x+tileSize < 0) || (x > windowSize);
	}

	private void drawCrossHair(Graphics g, int x, int y) {
		int r=30;
		g.drawArc(x-r/2, y-r/2, r, r, 0, 360);
		g.drawLine(x-r/2, y, x+r/2, y);
		g.drawLine(x, y-r/2, x, y+r/2);
	}
}

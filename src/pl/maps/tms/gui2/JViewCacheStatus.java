package pl.maps.tms.gui2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import javax.swing.JPanel;

import pl.maps.tms.TileGridProvider;
import pl.maps.tms.TiledPosition;
import pl.maps.tms.View;
import pl.maps.tms.cache.CacheEntry;
import pl.maps.tms.cache.TileCache;
import pl.maps.tms.cacheing.AsyncFetchListener;
import pl.maps.tms.cacheing.TileSpec;

public class JViewCacheStatus extends JComponent implements AsyncFetchListener {
	private View view;
	private TileCache cache;
	private BufferedImage img;

	private static enum CacheStatus {
		EMPTY(Color.GRAY), READY(Color.GREEN), PENDING(Color.YELLOW), ERROR(Color.RED);

		Color color;
		CacheStatus(Color c) {
			color = c;
		}
	}

	public Dimension getPreferredSize() {
		if(view == null)
			return super.getPreferredSize();
		return view.getTileCount();
	};
	
	public void setView(View view, TileCache cache) {
		this.view = view;
		this.cache = cache;
		TileGridProvider grid = view.getGrid();
		Dimension tc = view.getTileCount();
		img = new BufferedImage(tc.width + 1, tc.height + 1, BufferedImage.TYPE_3BYTE_BGR);
		updateImage(grid, img);
		JPanel p = (JPanel) getParent();
		p.revalidate();
	}

	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(Color.white);
		g.fillRect( 0, 0, getWidth(), getHeight());
		if(img == null)
			return;
		g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
	}

	private void updateImage(TileGridProvider grid, BufferedImage img) {
		TiledPosition pos = view.getPosition();
		int zoom = view.getZoom();
		Dimension tileCount = view.getTileCount();
		for (int y = 0; y <= tileCount.height; ++y) {
			for (int x = 0; x <= tileCount.width; ++x) {
				Point next = grid.adjacent(pos.tile, x, y);
				TileSpec spec = new TileSpec(next.x, next.y, zoom);
				CacheStatus stat = getCacheStatus(spec);
				int c = stat.color.getRGB();
				img.setRGB(x, y, c);
			}
		}
		repaint();
	}

	private CacheStatus getCacheStatus(TileSpec ts) {
		try {
			CacheEntry entry = cache.getCacheEntry(ts);
			if (entry == null)
				return CacheStatus.EMPTY;
			return entry.isReady() ? CacheStatus.READY : CacheStatus.PENDING;
		} catch (Exception e) {
			return CacheStatus.ERROR;
		}
	}

	@Override
	public void imageFetched(Image image, TileSpec spec) throws Exception {
		TileGridProvider grid = view.getGrid();
		updateImage(grid, img);
		repaint();
	}
}

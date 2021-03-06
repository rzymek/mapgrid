package pl.maps.tms.gui2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import pl.maps.tms.Position;
import pl.maps.tms.TileGridProvider;
import pl.maps.tms.ViewInfo;
import pl.maps.tms.cache.CacheEntry;
import pl.maps.tms.cache.TileCache;
import pl.maps.tms.cacheing.AsyncFetchListener;
import pl.maps.tms.cacheing.TileSpec;

public class JViewCacheStatus extends JComponent implements AsyncFetchListener {
	private ViewInfo view;
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
	
	public void setView(ViewInfo view, TileCache cache) {
		this.view = view;
		this.cache = cache;
		TileGridProvider grid = view.getGrid();
		Dimension tc = view.getTileCount();
		img = new BufferedImage(tc.width + 1, tc.height + 1, BufferedImage.TYPE_3BYTE_BGR);
		updateImage(grid, img);
		JComponent p = (JComponent) getParent();
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
		final Position pos = view.getPosition();
		final int zoom = view.getZoom();
		final Dimension tileCount = view.getTileCount();
		final int px = (int) pos.x;
		final int py = (int) pos.y;
		for (int y = 0; y <= tileCount.height; ++y) {
			for (int x = 0; x <= tileCount.width; ++x) {
				TileSpec spec = new TileSpec(px+x, py+y, zoom);
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

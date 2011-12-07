package pl.maps.backup;

import java.awt.Dimension;

import pl.maps.tms.Position;
import pl.maps.tms.TileGridProvider;
import pl.maps.tms.ViewInfo;

public class ViewInfoImpl implements ViewInfo {
	private final int zoom;
	private final Dimension dimension;
	private TileGridProvider provider;


	public ViewInfoImpl(Dimension dimension, TileGridProvider provider, int zoom) {
		this.zoom = zoom;
		this.dimension = dimension;
		this.provider = provider;
	}

	@Override
	public int getZoom() {
		return zoom;
	}

	@Override
	public Dimension getTileCount() {
		return dimension;
	}

	@Override
	public Position getPosition() {
		return new Position(0,0);
	}

	@Override
	public TileGridProvider getGrid() {
		return provider;
	}
}

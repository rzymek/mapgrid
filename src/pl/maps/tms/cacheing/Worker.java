package pl.maps.tms.cacheing;

import java.awt.Image;
import java.util.concurrent.BlockingQueue;

import pl.maps.tms.TileImageProvider;

final class Worker extends Thread {
	private final TileImageProvider provider;
	private final BlockingQueue<TileSpec> queue;
	private AsyncFetchListener listener = null;
	
	public Worker(BlockingQueue<TileSpec> queue, TileImageProvider provider) {
		this.queue = queue;
		this.provider = provider;
	}

	public void setListener(AsyncFetchListener listener) {
		this.listener = listener;
	}

	@Override
	public void run() {
		for(;;) {
			try {
				TileSpec tile = queue.take();
				Image image = provider.getTile(tile.x, tile.y, tile.z);
				if (listener != null) {
					listener.imageFetched(image, tile);
				}
			} catch (InterruptedException e) {
				continue;
			} catch (Exception e) {				
				System.err.println("Worker:"+e);
			}
		}
	}
}
package pl.maps.tms.cacheing;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import pl.maps.tms.TileImageProvider;

public final class AsyncTileFercher {
	
	private BlockingQueue<TileSpec> queue;
	
	private Worker[] workers;
	
	public AsyncTileFercher(int threads, TileImageProvider provider) {
		workers = new Worker[threads];
		queue = new ArrayBlockingQueue<TileSpec>(1024);
		for (int i = 0; i < workers.length; i++) {
			workers[i] = new Worker(queue, provider);
			workers[i].start();
		}
	}
	
	public void setListener(AsyncFetchListener listener){
		for (Worker worker : workers) 
			worker.setListener(listener);
	}	
	
	public void fetch(TileSpec tile) throws Exception {
		System.out.println("remain: "+queue.remainingCapacity()+", "+queue.size());
		queue.put(tile);
	}
}

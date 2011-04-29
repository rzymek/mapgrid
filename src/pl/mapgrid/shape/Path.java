package pl.mapgrid.shape;

public class Path<T> implements Shape<T> {
	protected final T[] points;

	public Path(T[] points) {
		this.points = points;
	}

	@Override
	public T get(int i) {
		return points[i];
	}

	@Override
	public int count() {
		return points.length;
	};
	
}

package pl.mapgrid.shape;

public class Waypoint<T> implements Shape<T> {
	protected final T point;

	public Waypoint(T point) {
		this.point = point;
	}

	@Override
	public T get(int i) {
		return point;
	}

	@Override
	public int count() {
		return 1;
	}

}

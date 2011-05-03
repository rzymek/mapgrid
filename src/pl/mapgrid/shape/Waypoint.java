package pl.mapgrid.shape;

public class Waypoint<T> extends Shape<T> {
	protected final T point;
	public String label;
	
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

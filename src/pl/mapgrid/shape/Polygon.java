package pl.mapgrid.shape;

public class Polygon<T> extends Path<T>{

	public Polygon(T[] points) {
		super(points);
	}
	
	@Override
	public T get(int i) {
		return i == points.length ? super.get(0) : super.get(i);
	}
	
	@Override
	public int count() {
		return super.count()+1;
	}

}

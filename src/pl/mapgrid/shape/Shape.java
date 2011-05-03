package pl.mapgrid.shape;

public abstract class Shape<T> {
	public abstract T get(int i);
	public abstract int count();
	public Style style;
}

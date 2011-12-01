package pl.maps.tms;

public final class Position implements Cloneable{
	public double x;
	public double y;
	public Position() {
		this(0.0, 0.0);
	}
	
	public Position(double x, double y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public Position clone() {
		return new Position(x,y);
	}
	@Override
	public String toString() {
		return String.format("[%4.2f,%4.2f]", x, y);
	}
}

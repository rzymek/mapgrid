package pl.maps.tms.utils;

public class Range {
	public int min, max;

	public Range(int min, int max) {
		this.min = min;
		this.max = max;
	}

	public int narrow(int value) {
		if(value < min)
			return min;
		if(value > max)
			return max;
		return value;
	}

}

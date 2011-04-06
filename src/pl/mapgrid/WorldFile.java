package pl.mapgrid;

public class WorldFile {
	public double pixelSizeX;
	public double pixelSizeY;
	public double coordX;
	public double coordY;

	@Override
	public String toString() {
		return pixelSizeX+"\n"+pixelSizeY+"\n"+coordX+"\n"+coordY;
	}
}

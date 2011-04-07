package pl.mapgrid.actions;

import java.awt.image.BufferedImage;
import java.util.List;

import pl.mapgrid.HoughTransform;
import pl.mapgrid.gui.JMapGridMain;

public class DetectGridAction extends BackgroundAction {

	private final JMapGridMain main;

	public DetectGridAction(JMapGridMain main) {
		super(main.actions);
		this.main = main;
	}

	@Override
	public void run() {
		BufferedImage image = main.view.getImage();
		HoughTransform transform = new HoughTransform();
		transform.setObserver(main);
		List<double[]> lines = transform.findGrid(image);
		main.view.setLines(lines);
	}
	
	@Override
	public String toString() {
		return "Wykryj";
	}
}

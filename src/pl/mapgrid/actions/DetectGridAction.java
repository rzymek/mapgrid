package pl.mapgrid.actions;

import java.awt.image.BufferedImage;

import pl.mapgrid.HoughTransform;
import pl.mapgrid.gui.JMapGridMain;

public class DetectGridAction extends BackgroundAction {

	private final JMapGridMain main;

	public DetectGridAction(JMapGridMain main) {
		super(main.actions, main);
		this.main = main;
	}

	@Override
	public void run() {
		BufferedImage image = main.view.getImage();
		HoughTransform transform = new HoughTransform(main.houghConfig);
		transform.setObserver(main);
		main.lines = transform.findGrid(image);
		main.view.setShowGrid(true);
		main.view.setLines(main.lines);
		main.view.repaint();
	}
	
	@Override
	public String toString() {
		return "Wykryj";
	}
}

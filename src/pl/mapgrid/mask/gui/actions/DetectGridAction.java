package pl.mapgrid.mask.gui.actions;

import java.awt.image.BufferedImage;

import pl.mapgrid.gui.actions.BackgroundAction;
import pl.mapgrid.gui.actions.UIAction;
import pl.mapgrid.mask.HoughTransform;
import pl.mapgrid.mask.gui.JMaskGridMain;

public class DetectGridAction extends BackgroundAction implements UIAction {

	private final JMaskGridMain main;

	public DetectGridAction(JMaskGridMain main) {
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

	@Override
	public boolean enabled() {
		return main.view.getImage() != null;
	}
}

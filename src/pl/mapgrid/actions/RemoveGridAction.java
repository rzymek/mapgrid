package pl.mapgrid.actions;

import java.awt.image.BufferedImage;

import pl.mapgrid.MaskGrid;
import pl.mapgrid.actions.base.BackgroundAction;
import pl.mapgrid.actions.base.UIAction;
import pl.mapgrid.gui.JMapGridMain;

public class RemoveGridAction extends BackgroundAction implements UIAction {

	private final JMapGridMain main;

	public RemoveGridAction(JMapGridMain main) {
		super(main.actions, main);
		this.main = main;
	}

	@Override
	public void run() {
		BufferedImage image = main.view.getImage();
		MaskGrid maskGrid = new MaskGrid(main.maskConfig);
		maskGrid.setObserver(main);
		image = maskGrid.filter(image, main.lines);
		main.view.setShowGrid(false);
		main.view.setImage(image);
	}

	@Override
	public String toString() {
		return "Maskuj";
	}

	@Override
	public boolean enabled() {
		return main.view.getGrid() != null;
	}
}

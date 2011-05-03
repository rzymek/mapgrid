package pl.mapgrid.app.actions;

import java.awt.image.BufferedImage;

import pl.mapgrid.app.Main;
import pl.mapgrid.gui.actions.BackgroundAction;
import pl.mapgrid.gui.actions.UIAction;
import pl.mapgrid.mask.MaskGrid;

public class RemoveGridAction extends BackgroundAction implements UIAction {

	private final Main main;

	public RemoveGridAction(Main main) {
		super(main.actions, main);
		this.main = main;
	}

	@Override
	public void run() {
		BufferedImage image = main.view.getImage();
		MaskGrid maskGrid = new MaskGrid(main.getMaskConfig());
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

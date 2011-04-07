package pl.mapgrid.actions;

import java.awt.image.BufferedImage;
import pl.mapgrid.MaskGrid;
import pl.mapgrid.gui.JMapGridMain;

public class RemoveGridAction extends BackgroundAction {

	private final JMapGridMain main;

	public RemoveGridAction(JMapGridMain main) {
		super(main.actions);
		this.main = main;
	}

	@Override
	public void run() {
		BufferedImage image = main.view.getImage();
		MaskGrid maskGrid = new MaskGrid();
		maskGrid.setObserver(main);
		image = maskGrid.filter(image, main.view.getLines());
		main.view.setLines(null);
		main.view.setImage(image);
	}

	@Override
	public String toString() {
		return "Maskuj";
	}
}

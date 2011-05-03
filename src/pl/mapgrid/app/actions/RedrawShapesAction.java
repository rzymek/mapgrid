package pl.mapgrid.app.actions;

import pl.mapgrid.app.Main;
import pl.mapgrid.gui.actions.BackgroundAction;
import pl.mapgrid.gui.actions.UIAction;

public class RedrawShapesAction extends BackgroundAction implements UIAction {

	private final Main main;

	public RedrawShapesAction(Main main) {
		super(main.actions, main);
		this.main = main;
	}

	@Override
	public void run() {
		main.view.setShapes(main.shapes, main.getShapeConfig());
	}

	@Override
	public boolean enabled() {
		return main.shapes != null;
	}

	@Override
	public String toString() {
		return "Odświerz kształty";
	}
}

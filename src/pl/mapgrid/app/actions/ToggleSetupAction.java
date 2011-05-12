package pl.mapgrid.app.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import pl.mapgrid.app.Main;
import pl.mapgrid.calibration.coordinates.Coordinates;
import pl.mapgrid.gui.actions.UIAction;

public class ToggleSetupAction extends AbstractAction implements UIAction {

	private final Main main;

	public ToggleSetupAction(Main main) {
		this.main = main;
	}

	@Override
	public void actionPerformed(ActionEvent paramActionEvent) {
		System.out.println(main.calibration);
		Coordinates[] c = main.calibration.coordinates;
		int width = (int) (c[1].getX() - c[0].getX()); 
		int height = (int) Math.abs(c[3].getY() - c[0].getY());
		System.out.println(width);
		System.out.println(height);
		main.setup.setVisible(!main.setup.isVisible());
		main.actions.reenable();
	}
	
	@Override
	public String toString() {
		return "Ustawienia";
	}

	@Override
	public boolean enabled() {
		return true;
	}

}

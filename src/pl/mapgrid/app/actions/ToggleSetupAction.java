package pl.mapgrid.app.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import pl.mapgrid.app.Main;
import pl.mapgrid.gui.actions.UIAction;

public class ToggleSetupAction extends AbstractAction implements UIAction {

	private final Main main;

	public ToggleSetupAction(Main main) {
		this.main = main;
	}

	@Override
	public void actionPerformed(ActionEvent paramActionEvent) {
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

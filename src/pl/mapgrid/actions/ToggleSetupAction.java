package pl.mapgrid.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import pl.mapgrid.actions.base.UIAction;
import pl.mapgrid.gui.JMapGridMain;

public class ToggleSetupAction extends AbstractAction implements UIAction {

	private final JMapGridMain main;

	public ToggleSetupAction(JMapGridMain main) {
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

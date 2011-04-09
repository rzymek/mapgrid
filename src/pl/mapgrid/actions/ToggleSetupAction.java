package pl.mapgrid.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import pl.mapgrid.gui.JMapGridMain;

public class ToggleSetupAction extends AbstractAction {

	private final JMapGridMain main;

	public ToggleSetupAction(JMapGridMain main) {
		this.main = main;
	}

	@Override
	public void actionPerformed(ActionEvent paramActionEvent) {
		main.setup.setVisible(!main.setup.isVisible());
	}
	
	@Override
	public String toString() {
		return "Ustawienia";
	}

}

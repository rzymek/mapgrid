package pl.mapgrid.mask.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import pl.mapgrid.gui.actions.UIAction;
import pl.mapgrid.mask.gui.JMaskGridMain;

public class ToggleSetupAction extends AbstractAction implements UIAction {

	private final JMaskGridMain main;

	public ToggleSetupAction(JMaskGridMain main) {
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

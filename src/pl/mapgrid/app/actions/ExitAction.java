package pl.mapgrid.app.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import pl.mapgrid.gui.actions.UIAction;

public class ExitAction extends AbstractAction implements UIAction {

	@Override
	public void actionPerformed(ActionEvent e) {
		System.exit(0);
	}
	
	@Override
	public String toString() {
		return "Koniec";
	}

	@Override
	public boolean enabled() {
		return true;
	}
}

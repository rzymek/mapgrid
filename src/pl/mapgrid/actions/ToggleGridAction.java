package pl.mapgrid.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import pl.mapgrid.gui.JMapGridMain;

public class ToggleGridAction extends AbstractAction {

	private final JMapGridMain main;

	public ToggleGridAction(JMapGridMain main) {
		this.main = main;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		main.view.toogleShowGrid();
	}
	
	@Override
	public String toString() {
		return "Przełącz wyświerlanie siatki";
	}

}

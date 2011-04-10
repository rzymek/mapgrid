package pl.mapgrid.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import pl.mapgrid.actions.base.UIAction;
import pl.mapgrid.gui.JMapGridMain;

public class ToggleGridAction extends AbstractAction implements UIAction {

	private final JMapGridMain main;

	public ToggleGridAction(JMapGridMain main) {
		this.main = main;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		main.view.toogleShowGrid();
		main.actions.reenable();
	}
	
	@Override
	public String toString() {
		return "Pokaż/Ukryj wykrytą siatkę";
	}

	@Override
	public boolean enabled() {
		return main.view.getGrid()!=null;
	}

}

package pl.mapgrid.app.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import pl.mapgrid.app.Main;
import pl.mapgrid.gui.actions.UIAction;

public class ToggleGridAction extends AbstractAction implements UIAction {

	private final Main main;

	public ToggleGridAction(Main main) {
		this.main = main;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		main.view.toogleShowGrid();
		main.actions.reenable();
	}
	
	@Override
	public String toString() {
		return "Siatka";
	}

	@Override
	public boolean enabled() {
		return main.view.getGrid()!=null;
	}

}

package pl.mapgrid.mask.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import pl.mapgrid.gui.actions.UIAction;
import pl.mapgrid.mask.gui.JMaskGridMain;

public class ToggleGridAction extends AbstractAction implements UIAction {

	private final JMaskGridMain main;

	public ToggleGridAction(JMaskGridMain main) {
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

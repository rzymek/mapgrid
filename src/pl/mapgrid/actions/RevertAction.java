package pl.mapgrid.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;

import pl.mapgrid.actions.base.UIAction;
import pl.mapgrid.gui.FileChooserSingleton;
import pl.mapgrid.gui.JMapGridMain;

public class RevertAction extends AbstractAction implements UIAction{

	private final JMapGridMain main;

	public RevertAction(JMapGridMain main) {
		this.main = main;
	}

	@Override
	public void actionPerformed(ActionEvent paramActionEvent) {
		File selectedFile = getSelectedFile();
		main.open(selectedFile);
		main.actions.reenable();
	}

	private File getSelectedFile() {
		return FileChooserSingleton.instance().getSelectedFile();
	}
	
	@Override
	public String toString() {
		return "Przywróć";
	}

	@Override
	public boolean enabled() {
		return getSelectedFile() != null;
	}
}

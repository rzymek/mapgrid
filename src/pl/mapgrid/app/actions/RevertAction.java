package pl.mapgrid.app.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;

import pl.mapgrid.app.Main;
import pl.mapgrid.gui.FileChooserSingleton;
import pl.mapgrid.gui.actions.UIAction;

public class RevertAction extends AbstractAction implements UIAction{

	private final Main main;

	public RevertAction(Main main) {
		this.main = main;
	}

	@Override
	public void actionPerformed(ActionEvent paramActionEvent) {
		File selectedFile = getSelectedFile();
		main.open(selectedFile);
		main.actions.reenable();
	}

	private File getSelectedFile() {
		return main.file;
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

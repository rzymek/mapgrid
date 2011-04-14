package pl.mapgrid.mask.gui.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;

import pl.mapgrid.gui.FileChooserSingleton;
import pl.mapgrid.gui.actions.UIAction;
import pl.mapgrid.mask.gui.JMaskGridMain;

public class RevertAction extends AbstractAction implements UIAction{

	private final JMaskGridMain main;

	public RevertAction(JMaskGridMain main) {
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

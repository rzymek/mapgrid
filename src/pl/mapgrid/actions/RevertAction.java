package pl.mapgrid.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;

import pl.mapgrid.gui.FileChooserSingleton;
import pl.mapgrid.gui.JMapGridMain;

public class RevertAction extends AbstractAction{

	private final JMapGridMain main;

	public RevertAction(JMapGridMain main) {
		this.main = main;
	}

	@Override
	public void actionPerformed(ActionEvent paramActionEvent) {
		File selectedFile = FileChooserSingleton.instance().getSelectedFile();
		main.open(selectedFile);
	}
	
	@Override
	public String toString() {
		return "Przywróć";
	}
}

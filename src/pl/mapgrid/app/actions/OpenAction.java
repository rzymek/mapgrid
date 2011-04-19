package pl.mapgrid.app.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;

import pl.mapgrid.app.Main;
import pl.mapgrid.gui.FileChooserSingleton;
import pl.mapgrid.gui.actions.BackgroundAction;
import pl.mapgrid.gui.actions.UIAction;

public class OpenAction extends BackgroundAction implements UIAction {
	private final Main main;

	public OpenAction(Main main) {
		super(main.actions, main);
		this.main = main;
	}

	@Override
	public void run() {
		try {	
			JFileChooser chooser = FileChooserSingleton.instance();
			chooser.setCurrentDirectory(new File("./input/"));
			int result = chooser.showOpenDialog(main);
			if(result == JFileChooser.APPROVE_OPTION) {
				main.open(chooser.getSelectedFile());
			}
		}catch (Exception ex) {
			throw new RuntimeException(ex);
		}finally{
			main.actions.reenable();
		}
	}

	@Override
	public String toString() {
		return "Otwórz";
	}

	@Override
	public boolean enabled() {
		return true;
	}

}

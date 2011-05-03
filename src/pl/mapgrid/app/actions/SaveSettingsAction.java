package pl.mapgrid.app.actions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.swing.JFileChooser;

import pl.mapgrid.app.Main;
import pl.mapgrid.gui.FileChooserSingleton;
import pl.mapgrid.gui.actions.BackgroundAction;
import pl.mapgrid.gui.actions.UIAction;

public class SaveSettingsAction extends BackgroundAction implements UIAction {

	private final Main main;

	public SaveSettingsAction(Main main) {
		super(main.actions, main);
		this.main = main;		
	}

	@Override
	public void run() {
		try {
			Serializable[] configs = main.configs;
			JFileChooser chooser = FileChooserSingleton.instance().getAnyChooser();
			int result = chooser.showSaveDialog(main);
			if(result == JFileChooser.APPROVE_OPTION) {
				File selectedFile = chooser.getSelectedFile();
				ObjectOutputStream out=null;
				try {
					out = new ObjectOutputStream(new FileOutputStream(selectedFile));
					for (Serializable serializable : configs) {
						out.writeObject(serializable);					
					}
					out.close();
				}catch (Exception e) {
					throw new RuntimeException(e);
				}finally{
					if(out != null)
						out.close();
				}
			}
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean enabled() {
		return true;
	}

	@Override
	public String toString() {
		return "Zapisz.cfg";
	}
}

package pl.mapgrid.app.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;

import javax.swing.JFileChooser;

import pl.mapgrid.app.Main;
import pl.mapgrid.gui.FileChooserSingleton;
import pl.mapgrid.gui.actions.BackgroundAction;
import pl.mapgrid.gui.actions.UIAction;

public class OpenSettingsAction extends BackgroundAction implements UIAction {

	private final Main main;

	public OpenSettingsAction(Main main) {
		super(main.actions, main);
		this.main = main;		
	}

	@Override
	public void run() {
		try {
			JFileChooser chooser = FileChooserSingleton.instance().getAnyChooser();
			int result = chooser.showOpenDialog(main);
			if(result == JFileChooser.APPROVE_OPTION) {
				File selectedFile = chooser.getSelectedFile();
				ObjectInputStream in = null;
				try {
					in = new ObjectInputStream(new FileInputStream(selectedFile));
					for(int i=0;i < main.configs.length;++i) 			
						main.configs[i] = (Serializable) in.readObject();
					main.setupConfigTabs();
					in.close();
				}catch (Exception e) {
					throw new RuntimeException(e);
				}finally{
					if(in != null)
						in.close();
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
		return "Otwórz.cfg";
	}
}

package pl.mapgrid.mask.gui.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;

import pl.mapgrid.gui.FileChooserSingleton;
import pl.mapgrid.gui.actions.UIAction;
import pl.mapgrid.mask.gui.JMaskGridMain;

public class OpenAction extends AbstractAction implements UIAction {
	private final JMaskGridMain main;

	public OpenAction(JMaskGridMain main) {
		this.main = main;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
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

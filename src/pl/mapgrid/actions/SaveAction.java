package pl.mapgrid.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;

import pl.mapgrid.gui.FileChooserSingleton;
import pl.mapgrid.gui.JMapGridMain;

public class SaveAction extends AbstractAction {

	private final JMapGridMain main;

	public SaveAction(JMapGridMain main) {
		this.main = main;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {	
			JFileChooser chooser = FileChooserSingleton.instance();
			int result = chooser.showSaveDialog(main);
			if(result == JFileChooser.APPROVE_OPTION) {
//				ImageIO.write(main.view.getImage(), chooser.getSelectedFile());
			}
		}catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	@Override
	public String toString() {
		return "Zapisz";
	}
}

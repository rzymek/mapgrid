package pl.mapgrid.actions;

import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;

import pl.mapgrid.gui.FileChooserSingleton;
import pl.mapgrid.gui.JMapGridMain;

public class SaveAction extends AbstractAction {
	private final List<String> imageSuffixes;
	private final JMapGridMain main;

	public SaveAction(JMapGridMain main) {
		this.main = main;
		imageSuffixes = Arrays.asList(ImageIO.getWriterFileSuffixes());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {	
			JFileChooser chooser = FileChooserSingleton.instance();
			int result = chooser.showSaveDialog(main);
			if(result == JFileChooser.APPROVE_OPTION) {
				String filename = chooser.getSelectedFile().getName();
				String ext = filename.substring(filename.lastIndexOf('.')+1);
				boolean r = ImageIO.write(main.view.getImage(), ext, chooser.getSelectedFile());
				if(r == false)
					throw new RuntimeException("Nie obsługiwany format pliku: "+ext+".\nObsługiwane: "+imageSuffixes);
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

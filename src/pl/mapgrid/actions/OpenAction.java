package pl.mapgrid.actions;

import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;

import pl.mapgrid.gui.FileChooserSingleton;
import pl.mapgrid.gui.JMapGridMain;

public class OpenAction extends AbstractAction {
	private final JMapGridMain main;

	public OpenAction(JMapGridMain main) {
		this.main = main;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {	
			JFileChooser chooser = FileChooserSingleton.instance();
			chooser.setCurrentDirectory(new File("./input/"));
			int result = chooser.showOpenDialog(main);
			if(result == JFileChooser.APPROVE_OPTION) {
				BufferedImage img = ImageIO.read(chooser.getSelectedFile());
				main.view.setImage(img);
			}
		}catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	@Override
	public String toString() {
		return "Otwórz";
	}

}

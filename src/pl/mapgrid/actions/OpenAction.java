package pl.mapgrid.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import pl.mapgrid.gui.JMapGridMain;

public class OpenAction extends AbstractAction {

	private final JMapGridMain main;

	public OpenAction(JMapGridMain main) {
		this.main = main;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
	
			JFileChooser chooser = new JFileChooser();
			FileFilter filter = new FileFilter() {
				
				@Override
				public String getDescription() {
					return Arrays.asList(ImageIO.getReaderFormatNames()).toString();
				}
				
				@Override
				public boolean accept(File f) {
					if(f.isDirectory())
						return true;
					String[] suffixes = ImageIO.getReaderFileSuffixes();
					for (String string : suffixes) {
						if(f.getName().toLowerCase().endsWith("."+string.toLowerCase()))
							return true;
					}
					return false;
				}
			};
			chooser.setFileFilter(filter);
			int result = chooser.showOpenDialog(main);
			if(result == JFileChooser.APPROVE_OPTION) 
					main.view.setImage(ImageIO.read(chooser.getSelectedFile()));
		}catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

}

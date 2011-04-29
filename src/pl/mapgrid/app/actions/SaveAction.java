package pl.mapgrid.app.actions;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import pl.mapgrid.app.Main;
import pl.mapgrid.gui.FileChooserSingleton;
import pl.mapgrid.gui.actions.BackgroundAction;
import pl.mapgrid.gui.actions.UIAction;

public class SaveAction extends BackgroundAction implements UIAction {
	private final List<String> imageSuffixes;
	private final Main main;

	public SaveAction(Main main) {
		super(main.actions, main);
		this.main = main;
		imageSuffixes = Arrays.asList(ImageIO.getWriterFileSuffixes());
	}

	@Override
	public void run() {
		try {	
			JFileChooser chooser = FileChooserSingleton.instance().getMapChooser();
			int result = chooser.showSaveDialog(main);
			if(result == JFileChooser.APPROVE_OPTION) {
				File selectedFile = chooser.getSelectedFile();
				if(selectedFile.exists()) {
					int res = JOptionPane.showConfirmDialog(main, "Nadpisać plik "+selectedFile.getName()+"?", "Plik istnieje", JOptionPane.YES_NO_OPTION);
					if(res != JOptionPane.YES_OPTION)
						return;
				}
				String filename = selectedFile.getName();
				String ext = filename.substring(filename.lastIndexOf('.')+1);
				BufferedImage image = main.view.getImageWithGrid();
				boolean r = ImageIO.write(image, ext, selectedFile);
				if(r == false)
					throw new RuntimeException("Nie obsługiwany format pliku: "+ext+".\nObsługiwane: "+imageSuffixes);
			}
		}catch (Exception ex) {
			throw new RuntimeException(ex);
		}finally{
			main.actions.reenable();
		}
	}
	@Override
	public String toString() {
		return "Zapisz";
	}
	
	public boolean enabled() {
		return main.view.getImage() != null;
	}
}

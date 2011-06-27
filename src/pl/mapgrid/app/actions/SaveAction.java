package pl.mapgrid.app.actions;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import pl.mapgrid.app.Main;
import pl.mapgrid.calibration.coordinates.Coordinates;
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
				if(main.calibration != null) {
					String path = selectedFile.getPath();
					filename = path.substring(0, path.lastIndexOf('.'))+".tfw";
					File tfw = new File(filename);
					if(tfw.exists()) {
						int res = JOptionPane.showConfirmDialog(main, "Nadpisać plik "+tfw.getName()+"?", "Plik istnieje", JOptionPane.YES_NO_OPTION);
						if(res != JOptionPane.YES_OPTION)
							return;
					}
					Coordinates[] c = main.calibration.toPuwg();
					double mapWidth = c[1].getX()-c[0].getX();
					double mapHeight = c[3].getY()-c[0].getY();
					image.getWidth();
					FileWriter tout = new FileWriter(tfw);
					try {
						tout.append(String.valueOf(mapWidth / image.getWidth())).append('\n');
						tout.append("0\n");
						tout.append("0\n");
						tout.append(String.valueOf(mapHeight / image.getHeight())).append('\n');
						tout.append(String.valueOf(c[0].getX())).append('\n');
						tout.append(String.valueOf(c[0].getY())).append('\n');
					}finally{
						tout.close();
					};
				}
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

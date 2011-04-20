package pl.mapgrid.gui;

import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

import pl.mapgrid.calibration.readers.Registry;
import pl.mapgrid.utils.RegistryFileFilter;

public class FileChooserSingleton {
	private static FileChooserSingleton instance;
	private JFileChooser chooser;
	private FileChooserSingleton() {
		chooser = new JFileChooser(new File("."));
		RegistryFileFilter images = new RegistryFileFilter("Pliki graficzne: ",
				ImageIO.getReaderFileSuffixes());
		RegistryFileFilter calibrated = new RegistryFileFilter("Pliki kalibracyjne: ", 
				Registry.getReaderFileSuffixes());
		chooser.addChoosableFileFilter(calibrated);
		chooser.addChoosableFileFilter(images);
	}
	public static JFileChooser instance() {
		if(instance == null)
			instance = new FileChooserSingleton();
		return instance.chooser;
	}
}

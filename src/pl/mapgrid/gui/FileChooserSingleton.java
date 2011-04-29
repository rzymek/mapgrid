package pl.mapgrid.gui;

import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import pl.mapgrid.calibration.readers.Registry;
import pl.mapgrid.utils.RegistryFileFilter;

public class FileChooserSingleton {
	private static FileChooserSingleton instance;
	private JFileChooser chooser;
	private FileFilter[] mapFilters;
	private FileFilter[] featureFilters;
	
	private FileChooserSingleton() {
		chooser = new JFileChooser(new File("."));
		mapFilters = new FileFilter[] {
			new RegistryFileFilter("Pliki graficzne: ",	ImageIO.getReaderFileSuffixes()),
			new RegistryFileFilter("Pliki kalibracyjne: ", Registry.getReaderFileSuffixes()),
		};
		featureFilters = new FileFilter[] {
			new RegistryFileFilter("Google Earth", Registry.getFeatureFileSuffixes())
		};
	}
	
	public JFileChooser getMapChooser() {
		return assignFilters(mapFilters);
	}
	
	public JFileChooser getFeatureChooser() {
		return assignFilters(featureFilters);
	}

	public static FileChooserSingleton instance() {
		if(instance == null)
			instance = new FileChooserSingleton();
		return instance;
	}

	private JFileChooser assignFilters(FileFilter[] filters) {
		chooser.resetChoosableFileFilters();
		for (FileFilter filter : filters) 
			chooser.addChoosableFileFilter(filter);
		return chooser;
	}
}

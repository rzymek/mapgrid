package pl.mapgrid.gui;

import java.awt.Component;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import pl.mapgrid.calibration.readers.Registry;
import pl.mapgrid.utils.RegistryFileFilter;

public class FileChooserSingleton {
	private static FileChooserSingleton instance;
	private final JFileChooser chooser;
	private final FileFilter[] mapFilters;
	private final FileFilter[] featureFilters;
	private final FileFilter[] imagesFilters;
	
	private FileChooserSingleton() {
		chooser = new JFileChooser(new File("."));
		RegistryFileFilter graphical = new RegistryFileFilter("Pliki graficzne: ",	ImageIO.getReaderFileSuffixes());
		mapFilters = new FileFilter[] {
			new RegistryFileFilter("Pliki kalibracyjne: ", Registry.getReaderFileSuffixes()),
			graphical,
		};
		featureFilters = new FileFilter[] {
			new RegistryFileFilter("Google Earth: ", Registry.getFeatureFileSuffixes())
		};
		imagesFilters = new FileFilter[] {
			graphical,
		};
	}
	
	public JFileChooser getMapChooser() {
		return assignFilters(mapFilters);
	}

	public JFileChooser getImageChooser() {
		return assignFilters(imagesFilters);
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
		if(filters.length > 0)
			chooser.setFileFilter(filters[0]);
		return chooser;
	}

	public JFileChooser getAnyChooser() {
		return assignFilters(new FileFilter[0]);
	}

	public static File getSelectedFile(JFileChooser chooser, Component parent) throws Abort {
		int result = chooser.showSaveDialog(parent);
		if(result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = chooser.getSelectedFile();				
			if(selectedFile.exists()) {
				int res = JOptionPane.showConfirmDialog(parent, 
						"Nadpisać plik "+selectedFile.getName()+"?", 
						"Plik istnieje", JOptionPane.YES_NO_OPTION);
				if(res != JOptionPane.YES_OPTION)
					throw new Abort();
			}
			return selectedFile;
		}
		throw new Abort();
	}

}

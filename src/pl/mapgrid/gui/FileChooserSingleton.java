package pl.mapgrid.gui;

import javax.swing.JFileChooser;

import pl.mapgrid.utils.GeneralImageFileFilter;

public class FileChooserSingleton {
	private static FileChooserSingleton instance;
	private JFileChooser chooser;
	private FileChooserSingleton() {
		chooser = new JFileChooser();
		chooser.setFileFilter(new GeneralImageFileFilter());
	}
	public static JFileChooser instance() {
		if(instance == null)
			instance = new FileChooserSingleton();
		return instance.chooser;
	}
}

package pl.mapgrid.gui;

import javax.swing.JFileChooser;

import pl.mapgrid.actions.ImageFileFilter;

public class FileChooserSingleton {
	private static FileChooserSingleton instance;
	private JFileChooser chooser;
	private FileChooserSingleton() {
		chooser = new JFileChooser();
		chooser.setFileFilter(new ImageFileFilter());
	}
	public static JFileChooser instance() {
		if(instance == null)
			instance = new FileChooserSingleton();
		return instance.chooser;
	}
}

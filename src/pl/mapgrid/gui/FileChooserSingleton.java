package pl.mapgrid.gui;

import java.io.File;

import javax.swing.JFileChooser;

import pl.mapgrid.utils.ImageFileFilter;

public class FileChooserSingleton {
	private static FileChooserSingleton instance;
	private JFileChooser chooser;
	private FileChooserSingleton() {
		chooser = new JFileChooser(new File("."));
		chooser.setFileFilter(new ImageFileFilter());
	}
	public static JFileChooser instance() {
		if(instance == null)
			instance = new FileChooserSingleton();
		return instance.chooser;
	}
}

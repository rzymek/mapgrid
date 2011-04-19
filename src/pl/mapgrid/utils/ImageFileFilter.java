package pl.mapgrid.utils;

import java.io.File;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileFilter;

public class ImageFileFilter extends FileFilter {
	@Override
	public String getDescription() {
		Set<String> uniq = new TreeSet<String>();					
		for (String format : ImageIO.getReaderFileSuffixes()) {
			if(format.trim().isEmpty())
				continue;
			uniq.add(format.toLowerCase());
		}
		StringBuilder desc = new StringBuilder("Pliki graficzne: ");
		Iterator<String> iterator = uniq.iterator();
		while (iterator.hasNext()) {
			desc.append(iterator.next());
			if(iterator.hasNext())
				desc.append(", ");
		}
		return desc.toString();					
	}

	@Override
	public boolean accept(File f) {
		if(f.isDirectory())
			return true;
		String ext = f.getName().toLowerCase();
		if(ext.endsWith(".map"))
			return true;
		String[] suffixes = ImageIO.getReaderFileSuffixes();
		for (String string : suffixes) {
			if(ext.endsWith("."+string.toLowerCase()))
				return true;
		}
		return false;
	}
}
package pl.mapgrid.utils;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.filechooser.FileFilter;

public class RegistryFileFilter extends FileFilter {

	private final String prefix;
	private final List<String> suffixes;

	public RegistryFileFilter(String prefix, List<String> suffixes) {
		this.prefix = prefix;
		this.suffixes = suffixes;
	}

	public RegistryFileFilter(String prefix, String[] suffixes) {
		this(prefix, Arrays.asList(suffixes));
	}

	@Override
	public String getDescription() {
		Set<String> uniq = new TreeSet<String>();					
		for (String format : suffixes ) {
			if(format.trim().isEmpty())
				continue;
			uniq.add(format.toLowerCase());
		}
		StringBuilder desc = new StringBuilder(prefix);
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
		for (String string : suffixes) {
			if(ext.endsWith("."+string.toLowerCase()))
				return true;
		}
		return false;
	}
}

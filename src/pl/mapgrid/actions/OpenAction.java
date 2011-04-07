package pl.mapgrid.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import pl.mapgrid.gui.JMapGridMain;

public class OpenAction extends AbstractAction {

	private final JMapGridMain main;
	private JFileChooser chooser;

	public OpenAction(JMapGridMain main) {
		this.main = main;
		chooser = new JFileChooser();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {	
			FileFilter filter = new FileFilter() {
				
				@Override
				public String getDescription() {
					Set<String> uniq = new TreeSet<String>();					
					for (String format : ImageIO.getReaderFormatNames()) {
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
					String[] suffixes = ImageIO.getReaderFileSuffixes();
					for (String string : suffixes) {
						if(f.getName().toLowerCase().endsWith("."+string.toLowerCase()))
							return true;
					}
					return false;
				}
			};
			chooser.setFileFilter(filter);
			int result = chooser.showOpenDialog(main);
			if(result == JFileChooser.APPROVE_OPTION) 
					main.view.setImage(ImageIO.read(chooser.getSelectedFile()));
		}catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	@Override
	public String toString() {
		return "Otwórz";
	}

}

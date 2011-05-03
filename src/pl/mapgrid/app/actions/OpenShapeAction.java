package pl.mapgrid.app.actions;

import java.io.File;

import javax.swing.JFileChooser;

import pl.mapgrid.app.Main;
import pl.mapgrid.calibration.readers.KMLReader;
import pl.mapgrid.calibration.readers.Registry;
import pl.mapgrid.gui.FileChooserSingleton;
import pl.mapgrid.gui.actions.BackgroundAction;
import pl.mapgrid.gui.actions.UIAction;

public class OpenShapeAction extends BackgroundAction implements UIAction {

	private final Main main;

	public OpenShapeAction(Main main) {
		super(main.actions, main);
		this.main = main;
	}

	@Override
	public void run() {
		try {	
			JFileChooser chooser = FileChooserSingleton.instance().getFeatureChooser();
			chooser.setCurrentDirectory(new File("./input/"));
			int result = chooser.showOpenDialog(main);
			if(result == JFileChooser.APPROVE_OPTION) {			
				open(chooser.getSelectedFile());
			}
		}catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	private void open(File file) throws Exception {
		KMLReader reader = (KMLReader) Registry.getFeatureReader(file.getName());
		main.shapes = reader.read(file);
		main.view.setShapes(main.shapes, main.getShapeConfig());
	}

	@Override
	public boolean enabled() {
		return main.view.getImage() != null;
	}

	@Override
	public String toString() {
		return "Kształty";
	}
}

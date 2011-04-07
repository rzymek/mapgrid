package pl.mapgrid.actions;

import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

import javax.swing.AbstractAction;

import pl.mapgrid.JImageView;

public class DetectGridAction extends AbstractAction {

	private final JImageView view;

	public DetectGridAction(JImageView view) {
		this.view = view;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		BufferedImage image = view.getImage();		
	}

}

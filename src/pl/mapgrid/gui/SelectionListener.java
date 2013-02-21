package pl.mapgrid.gui;

import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SelectionListener extends MouseAdapter {

	@Override
	public void mousePressed(MouseEvent e) {
		if((e.getModifiers() & InputEvent.CTRL_MASK) != 0) {
			System.out.println("SelectionListener.mousePressed()");
		}
	}
}

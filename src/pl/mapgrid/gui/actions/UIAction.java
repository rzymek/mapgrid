package pl.mapgrid.gui.actions;

import javax.swing.Action;

public interface UIAction extends Action {

	boolean enabled();
	@Override
	String toString();
}

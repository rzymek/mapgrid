package pl.mapgrid;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

public class Actions {

	public static final Action OPEN = new OpenAction();
	public static final Action EXIT = new ExitAction(); 
	
	private static class ExitAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	};

}

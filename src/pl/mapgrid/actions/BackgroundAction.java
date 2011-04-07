package pl.mapgrid.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.SwingWorker;

public abstract class BackgroundAction extends AbstractAction implements Runnable {
	private final Actions actions;
	public BackgroundAction(Actions actions) {
		this.actions = actions;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		actions.setEnabled(this, false);
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				try {
					BackgroundAction.this.run();
				}finally{
					actions.setEnabled(BackgroundAction.this, true);
				}
				return null;
			}			
		};
		worker.execute();
	}
}

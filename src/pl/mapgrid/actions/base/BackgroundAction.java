package pl.mapgrid.actions.base;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.SwingWorker;

import pl.mapgrid.ProgressMonitor;

public abstract class BackgroundAction extends AbstractAction implements
		Runnable {
	private final Actions actions;
	private final ProgressMonitor monitor;

	public BackgroundAction(Actions actions, ProgressMonitor monitor) {
		this.actions = actions;
		this.monitor = monitor;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		actions.setEnabled(this, false);
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				monitor.setProgressMessage(BackgroundAction.this.toString());
				try {
					BackgroundAction.this.run();
				} finally {
					actions.setEnabled(BackgroundAction.this, true);
					monitor.updateProgress(0);
					monitor.setProgressMessage("");
				}
				return null;
			}
		};
		worker.execute();
	}
}

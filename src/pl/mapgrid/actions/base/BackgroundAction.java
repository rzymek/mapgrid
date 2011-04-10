package pl.mapgrid.actions.base;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.SwingWorker;

import pl.mapgrid.ProgressMonitor;

public abstract class BackgroundAction extends AbstractAction implements Runnable {
	private final ProgressMonitor monitor;
	private final Actions actions;

	public BackgroundAction(Actions actions, ProgressMonitor monitor) {
		this.actions = actions;
		this.monitor = monitor;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		setEnabled(false);
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				monitor.setProgressMessage(BackgroundAction.this.toString());
				try {
					BackgroundAction.this.run();
				} finally {
					monitor.updateProgress(0);
					monitor.setProgressMessage("");
					actions.reenable();
				}
				return null;
			}
		};
		worker.execute();
	}
}

package pl.mapgrid.gui.actions;

import java.awt.event.ActionEvent;
import java.lang.Thread.UncaughtExceptionHandler;

import javax.swing.AbstractAction;
import javax.swing.SwingWorker;

import pl.mapgrid.mask.gui.Actions;
import pl.mapgrid.utils.ProgressMonitor;

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
		final UncaughtExceptionHandler exceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				monitor.setProgressMessage(BackgroundAction.this.toString());
				try {
					BackgroundAction.this.run();
				}catch (Exception e) {
					exceptionHandler.uncaughtException(Thread.currentThread(), e);
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

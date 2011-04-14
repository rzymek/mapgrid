package pl.mapgrid.gui;

import java.lang.Thread.UncaughtExceptionHandler;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public abstract class JMainFrame extends JFrame implements UncaughtExceptionHandler, Runnable {
	public JMainFrame() {
		Thread.setDefaultUncaughtExceptionHandler(this);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	@Override
	public void run() {
		Thread.setDefaultUncaughtExceptionHandler(this);
		setVisible(true);
	}
	
	@Override
	public void uncaughtException(Thread thread, Throwable exception) {
		exception = getCause(exception);
		JOptionPane.showMessageDialog(this, exception.toString());
	}

	private Throwable getCause(Throwable exception) {
		Throwable cause = exception.getCause();
		return cause == null ? exception : getCause(cause);
	}
}

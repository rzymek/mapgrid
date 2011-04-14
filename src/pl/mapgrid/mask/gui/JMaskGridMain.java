package pl.mapgrid.mask.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import pl.mapgrid.gui.JForm;
import pl.mapgrid.gui.JImageView;
import pl.mapgrid.mask.HoughTransform;
import pl.mapgrid.mask.MaskGrid;
import pl.mapgrid.mask.gui.Actions.Name;
import pl.mapgrid.mask.gui.actions.DetectGridAction;
import pl.mapgrid.mask.gui.actions.ExitAction;
import pl.mapgrid.mask.gui.actions.OpenAction;
import pl.mapgrid.mask.gui.actions.RemoveGridAction;
import pl.mapgrid.mask.gui.actions.RevertAction;
import pl.mapgrid.mask.gui.actions.SaveAction;
import pl.mapgrid.mask.gui.actions.ToggleGridAction;
import pl.mapgrid.mask.gui.actions.ToggleSetupAction;
import pl.mapgrid.utils.DragableViewportMouseListener;
import pl.mapgrid.utils.ProgressMonitor;

public class JMaskGridMain extends JFrame implements ProgressMonitor, UncaughtExceptionHandler {
	public JImageView view;
	public JProgressBar status;
	public Actions actions;
	public List<double[]> lines;
	public HoughTransform.Config houghConfig = new HoughTransform.Config();
	public MaskGrid.Config maskConfig = new MaskGrid.Config();
	public JForm setup;

	public JMaskGridMain() throws Exception {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Maskuj siatkę");
		setupActions();
		setupComponents();
		actions.reenable();
	}
	
	private void setupActions() {
		actions = new Actions();
		actions.set(Actions.Name.OPEN, new OpenAction(this));
		actions.set(Actions.Name.REVERT, new RevertAction(this));
		actions.set(Actions.Name.SAVE, new SaveAction(this));
		actions.set(Actions.Name.EXIT, new ExitAction());
		actions.set(Actions.Name.DETECT_GRID, new DetectGridAction(this));
		actions.set(Actions.Name.REMOVE_GRID, new RemoveGridAction(this));
		actions.set(Actions.Name.TOGGLE_GRID, new ToggleGridAction(this));
		actions.set(Actions.Name.TOGGLE_SETUP, new ToggleSetupAction(this));
	}

	private void setupComponents() throws Exception {
		view = new JImageView();
		
		JToolBar toolbar = new JToolBar(JToolBar.HORIZONTAL);
		Name[] values = Actions.Name.values();
		for (Name name : values) 
			toolbar.add(createToolButton(name));

		status = new JProgressBar(0,100);
		status.setString("");
		status.setStringPainted(true);
		
		setup = new JForm(houghConfig, maskConfig);
		setup.setVisible(false);
		
		JScrollPane scroll = new JScrollPane(view);
		DragableViewportMouseListener drag = new DragableViewportMouseListener(scroll.getViewport());
		view.addMouseListener(drag);
		view.addMouseMotionListener(drag);
		layoutComponents(toolbar, scroll, setup, status);
	}

	private void layoutComponents(JComponent top, JComponent center, JComponent right, JComponent bottom) {
		Container pane = getContentPane();
		pane.add(top, BorderLayout.NORTH);
		pane.add(center, BorderLayout.CENTER);
		pane.add(right, BorderLayout.EAST);
		pane.add(bottom, BorderLayout.SOUTH);
		setPreferredSize(new Dimension(800,700));
		pack();
	}

	private JButton createToolButton(Name name) {
		JButton button = new JButton();
		button.setAction(actions.get(name));
		button.setText(actions.getDescription(name));
		return button;
	}

	public static void main(String[] args) throws Exception {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					JMaskGridMain main = new JMaskGridMain();
					Thread.setDefaultUncaughtExceptionHandler(main);
					main.setVisible(true);
				}catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});
	}

	@Override
	public void updateProgress(int percent) {
		status.setValue(percent);
	}

	@Override
	public void setProgressMessage(String string) {
		status.setString(string);
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

	public void open(File selectedFile) {
		try {
			BufferedImage img = ImageIO.read(selectedFile);
			view.setImage(img);
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}


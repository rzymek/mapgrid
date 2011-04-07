package pl.mapgrid.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import pl.mapgrid.ProgressMonitor;
import pl.mapgrid.actions.Actions;
import pl.mapgrid.actions.Actions.Name;
import pl.mapgrid.actions.DetectGridAction;
import pl.mapgrid.actions.ExitAction;
import pl.mapgrid.actions.OpenAction;
import pl.mapgrid.actions.RemoveGridAction;

public class JMapGridMain extends JFrame implements ProgressMonitor {
	public JImageView view;
	public JProgressBar status;
	public Actions actions;

	public JMapGridMain() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Map Grid");
		setupActions();
		setupComponents();
	}
	
	private void setupActions() {
		actions = new Actions();
		actions.set(Actions.Name.OPEN, new OpenAction(this));
		actions.set(Actions.Name.EXIT, new ExitAction());
		actions.set(Actions.Name.REMOVE_GRID, new RemoveGridAction(this));
		actions.set(Actions.Name.DETECT_GRID, new DetectGridAction(this));
	}

	private void setupComponents() {
		view = new JImageView();
		
		JToolBar toolbar = new JToolBar(JToolBar.HORIZONTAL);
		toolbar.add(createToolButton("Open", Actions.Name.OPEN));
		toolbar.add(createToolButton("Wykryj siatkę", Actions.Name.DETECT_GRID));
		toolbar.add(createToolButton("Maskuj siatkę", Actions.Name.REMOVE_GRID));
		toolbar.add(createToolButton("Exit", Actions.Name.EXIT));

		status = new JProgressBar(0,100);
		layoutComponents(toolbar, new JScrollPane(view), status);
	}

	private void layoutComponents(JComponent top, JComponent center, JComponent bottom) {
		Container pane = getContentPane();
		pane.add(top, BorderLayout.NORTH);
		pane.add(center, BorderLayout.CENTER);
		pane.add(bottom, BorderLayout.SOUTH);
		setPreferredSize(new Dimension(800,600));
		pack();
	}

	private JButton createToolButton(String string, Name name) {
		JButton button = new JButton();
		actions.assign(button, name);
		button.setText(string);
		return button;
	}

	public static void main(String[] args) throws IOException {
		JMapGridMain main = new JMapGridMain();
		main.setVisible(true); 
	}

	@Override
	public void update(int percent) {
		status.setValue(percent);
	}

}


package pl.mapgrid.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import pl.mapgrid.HoughTransform;
import pl.mapgrid.HoughTransform.Config;
import pl.mapgrid.MaskGrid;
import pl.mapgrid.ProgressMonitor;
import pl.mapgrid.actions.Actions;
import pl.mapgrid.actions.Actions.Name;
import pl.mapgrid.actions.DetectGridAction;
import pl.mapgrid.actions.ExitAction;
import pl.mapgrid.actions.OpenAction;
import pl.mapgrid.actions.RemoveGridAction;
import pl.mapgrid.actions.SaveAction;
import pl.mapgrid.actions.ToggleGridAction;

public class JMapGridMain extends JFrame implements ProgressMonitor {
	public JImageView view;
	public JProgressBar status;
	public Actions actions;
	public List<double[]> lines;
	public HoughTransform.Config houghConfig = new HoughTransform.Config();
	public MaskGrid.Config maskConfig = new MaskGrid.Config();

	public JMapGridMain() throws Exception {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Map Grid");
		setupActions();
		setupComponents();
	}
	
	private void setupActions() {
		actions = new Actions();
		actions.set(Actions.Name.OPEN, new OpenAction(this));
		actions.set(Actions.Name.SAVE, new SaveAction(this));
		actions.set(Actions.Name.EXIT, new ExitAction());
		actions.set(Actions.Name.DETECT_GRID, new DetectGridAction(this));
		actions.set(Actions.Name.REMOVE_GRID, new RemoveGridAction(this));
		actions.set(Actions.Name.TOGGLE_GRID, new ToggleGridAction(this));
	}

	private void setupComponents() throws Exception {
		view = new JImageView();
		
		JToolBar toolbar = new JToolBar(JToolBar.HORIZONTAL);
		toolbar.add(createToolButton(Actions.Name.OPEN));
		toolbar.add(createToolButton(Actions.Name.SAVE));
		toolbar.add(createToolButton(Actions.Name.DETECT_GRID));
		toolbar.add(createToolButton(Actions.Name.REMOVE_GRID));
		toolbar.add(createToolButton(Actions.Name.TOGGLE_GRID));
		toolbar.add(createToolButton(Actions.Name.EXIT));

		status = new JProgressBar(0,100);
		status.setString("");
		status.setStringPainted(true);
		
		JForm setup = new JForm(houghConfig, maskConfig);
		
		layoutComponents(toolbar, new JScrollPane(view), setup, status);
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
		actions.assign(button, name);
		button.setText(actions.getDescription(name));
		return button;
	}

	public static void main(String[] args) throws Exception {
		JMapGridMain main = new JMapGridMain();
		main.setVisible(true); 
	}

	@Override
	public void updateProgress(int percent) {
		status.setValue(percent);
	}

	@Override
	public void setProgressMessage(String string) {
		status.setString(string);
	}

}


package pl.mapgrid;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import pl.mapgrid.actions.Actions;
import pl.mapgrid.actions.DetectGridAction;
import pl.mapgrid.actions.ExitAction;
import pl.mapgrid.actions.OpenAction;
import pl.mapgrid.actions.RemoveGridAction;

public class JMapGridMain extends JFrame {
	private JImageView view;
	private JImageView hough;
	private JLabel status;
	private Actions actions;

	public JMapGridMain() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Map Grid");
		setupComponents();
		actions = setupActions();
	}
	
	private Actions setupActions() {
		Actions actions = new Actions();
		actions.set(Actions.Name.OPEN, new OpenAction(this));
		actions.set(Actions.Name.EXIT, new ExitAction());
		actions.set(Actions.Name.REMOVE_GRID, new RemoveGridAction(view));
		actions.set(Actions.Name.DETECT_GRID, new DetectGridAction(view));
		return actions;
	}

	private void setupComponents() {
		view = new JImageView();
		hough = new JImageView();
		
		JToolBar toolbar = new JToolBar(JToolBar.HORIZONTAL);
		toolbar.add(createToolButton("Open", actions.get(Actions.Name.OPEN)));
		toolbar.add(createToolButton("Wykryj siatkę", actions.get(Actions.Name.DETECT_GRID)));
		toolbar.add(createToolButton("Maskuj siatkę", actions.get(Actions.Name.REMOVE_GRID)));
		toolbar.add(createToolButton("Exit", actions.get(Actions.Name.EXIT)));

		status = new JLabel("Ready.");
//		JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, 
//				new JScrollPane(view), 
//				new JScrollPane(hough));	
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

	private JButton createToolButton(String string, Action action) {
		JButton button = new JButton();
		button.setAction(action);
		button.setText(string);
		return button;
	}

	public static void main(String[] args) throws IOException {
		JMapGridMain main = new JMapGridMain();
		main.open("input/rr3.png");
//		main.open("/home/rzymek/Pictures/maps/ramsar2010/topo25.png");
		main.setVisible(true); 
	}

	private void open(String string) throws IOException {
		BufferedImage img = ImageIO.read(new File(string));
//		ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);  
//		ColorConvertOp op = new ColorConvertOp(cs, null);  
//		img = op.filter(img, null);
//		for(int x=0;x<img.getWidth();x++)
//			for(int y=0;y<img.getHeight();y++) {
//				int value = HoughTransform.pixelValue(img, x, y);
//				img.setRGB(x, y, value < 255/2 ? 0 : 0xffffff);
//			}
		view.setImage(img);
		
		System.out.println("Hough...");
		HoughTransform ht = new HoughTransform();
		ht.tranform(view.getImage());
		view.setLines(ht.coord);
		System.out.println("done...");
//		hough.setImage(dest);
	}

}


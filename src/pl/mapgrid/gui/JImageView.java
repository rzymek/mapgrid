package pl.mapgrid.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JComponent;

import pl.mapgrid.mask.MaskGrid;

public class JImageView extends JComponent implements Observer {
	private BufferedImage image = null;
	private BufferedImage grid;
	private boolean showGrid = true;
	private float zoom = 1;

	public JImageView() {
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (image == null)
			return;	
		g.drawImage(image, 0, 0, (int) (image.getWidth() * zoom), (int) (image.getHeight() * zoom), this);
		if (showGrid != false && grid != null)
			g.drawImage(grid, 0, 0, (int) (image.getWidth() * zoom), (int) (image.getHeight() * zoom), this);
	}

	public void setImage(BufferedImage image) {
		this.image = image;
		setPreferredSize(new Dimension(image.getWidth(this), image.getHeight(this)));
		zoom = 1;
		repaint();
		revalidate();
	}

	public BufferedImage getImage() {
		return image;
	}
	public BufferedImage getImageWithGrid() {
		BufferedImage image = new BufferedImage(
				this.image.getWidth(), 
				this.image.getHeight(), 
				BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0,0,image.getWidth(), image.getHeight());
		g.drawImage(this.image, 0,0, this);
		g.drawImage(this.grid, 0,0, this);
		return image;
	}

	@Override
	public void update(Observable o, Object arg) {
	}

	public void setLines(List<double[]> lines) {
		grid = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = (Graphics2D) grid.getGraphics();
		g.setColor(new Color(255, 0, 0, 128));
		g.setStroke(new BasicStroke(6));
		int x1, x2, y1, y2;
		final double d45 = Math.PI / 8;
		for (double[] c : lines) {
			if (-d45 < c[0] && c[0] < d45) {
				// vertical
				y1 = 0;
				x1 = MaskGrid.lineX(y1, c[0], c[1]);
				y2 = image.getWidth();
				x2 = MaskGrid.lineX(y2, c[0], c[1]);
				g.drawLine(x1, y1, x2, y2);
			} else {
				// horizontal
				x1 = 0;
				y1 = MaskGrid.lineY(x1, c[0], c[1]);
				x2 = image.getWidth();
				y2 = MaskGrid.lineY(x2, c[0], c[1]);
				g.drawLine(x1, y1, x2, y2);
			}
		}
		repaint();
	}

	public void toogleShowGrid() {
		showGrid = !showGrid;
		repaint();
	}

	public void setShowGrid(boolean b) {
		showGrid = b;
		repaint();
	}

	public void setZoom(float zoom) {
		this.zoom = zoom;
		setPreferredSize(new Dimension((int)(image.getWidth(this)*zoom), (int)(image.getHeight(this)*zoom)));
		revalidate();
		repaint();
	}
	
	public BufferedImage getGrid() {
		return grid;
	}

	public void setGrid(List<int[]> vertical, List<int[]> horizontal,
			int firstEasting, int firstNorthing) {
		grid = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = (Graphics2D) grid.getGraphics();
		Color lineColor = new Color(0,0,0,128);
		Color[] borderColor = {
			new Color(0,0,0),
			new Color(255,255,255),
		};		
		//drawBorder
		int border=20;		
		g.setFont(new Font(Font.MONOSPACED, Font.BOLD, border));
		drawVertical(vertical, g, borderColor, border, firstEasting);
		drawHorizontal(horizontal, g, borderColor, border, firstNorthing);

		//draw grid
		g.setColor(lineColor);
		g.setStroke(new BasicStroke(3));
		for (int[] line : vertical) 
			g.drawLine(line[0], line[1], line[2], line[3]);
		for (int[] line : horizontal) 
			g.drawLine(line[0], line[1], line[2], line[3]);
		setShowGrid(true);
	}

	private void drawVertical(List<int[]> lines, Graphics2D g, Color[] borderColor, int border, int firstEasting) {
		int[] x = new int[4];
		int[] y = new int[4];
		int borderColorIndex = 0;
		for(int i=-1;i<lines.size();++i) {
			int x1,y1,x2,y2;
			if(i==-1) {
				x1 = 0;
				y1 = 0;
				x2 = 0;
				y2 = image.getHeight();
			}else{
				x1 = lines.get(i)[0];
				y1 = lines.get(i)[1];
				x2 = lines.get(i)[2];
				y2 = lines.get(i)[3];
			}
			int dx = x2-x1;
			int dy = y2-y1;
			int nx1;
			if(i+1 >= lines.size())
				nx1 = image.getWidth(); //last incomplete
			else
				nx1 = lines.get(i+1)[0];
			int bx = border * dx / dy;				
			/*<pre>
			 *   0 ------ 1
			 *   |        |
			 *   3--------2
			 *</pre>*/
			arrayCopy(x, x1, nx1, nx1+bx,		x1+bx);
			arrayCopy(y, y1, y1,  y1+border,	y1+border);
			g.setColor(borderColor[borderColorIndex]);
			g.fillPolygon(x, y, x.length);
			borderColorIndex = (borderColorIndex+1)%borderColor.length;
			g.setColor(borderColor[0]);
			g.drawPolygon(x, y, x.length);
			if(i >= 0 && (i+1 < lines.size())) {
				g.setColor(borderColor[borderColorIndex]);
				drawTextVertical(g, String.valueOf(firstEasting+1000*(i+1)), x ,y);
			}
		}
	}
	private void drawTextVertical(Graphics2D g, String s, int[] x, int[] y) {
		FontMetrics fm = g.getFontMetrics();
		int h = fm.getAscent();
		int w = fm.stringWidth(s);
		int xx = x[0] + (x[1]-x[0]-w)/2;
		int yy = y[0] + (y[3]-y[0]+h)/2-1;
		g.drawString(s, xx, yy);
	}

	private void drawTextHorizontal(Graphics2D g, String s, int[] x, int[] y) {
		FontMetrics fm = g.getFontMetrics();
		int h = fm.getAscent();
		int w = fm.stringWidth(s);
		int xx = x[0] + (x[1]-x[0])/2;
		int yy = y[0] + (y[3]-y[0])/2-1;
		AffineTransform save = g.getTransform();
		AffineTransform tx = new AffineTransform();
		tx.translate(xx, yy);		
		tx.rotate(Math.toRadians(270));
		g.setTransform(tx);
		g.drawString(s, -w/2, h/2);
		g.setTransform(save);
	}

	private void drawHorizontal(List<int[]> lines, Graphics2D g, Color[] borderColor, int border, int firstNorthing) {
		int[] x = new int[4];
		int[] y = new int[4];
		int borderColorIndex = 0;
		for(int i=-1;i<lines.size();++i) {
			int x1,y1,x2,y2;
			if(i==-1) {
				x1 = 0;
				y1 = 0;
				x2 = image.getWidth();//!
				y2 = 0;//!
			}else{
				x1 = lines.get(i)[0];
				y1 = lines.get(i)[1];
				x2 = lines.get(i)[2];
				y2 = lines.get(i)[3];
			}
			int dx = x2-x1;
			int dy = y2-y1;
			int n;
			if(i+1 >= lines.size())
				n = image.getHeight(); //!
			else
				n = lines.get(i+1)[1]; //!
			int b = border * dy / dx; //!				
			/*<pre>
			 *   0 ------ 1
			 *   |        |
			 *   3--------2
			 *</pre>*/
			arrayCopy(x, x1, x1+border, x1+border, 	x1);//!
			arrayCopy(y, y1, y1+b, 		n+b,		n);//!
			g.setColor(borderColor[borderColorIndex]);
			g.fillPolygon(x, y, x.length);
			borderColorIndex = (borderColorIndex+1)%borderColor.length;
			g.setColor(borderColor[0]);
			g.drawPolygon(x, y, x.length);
			if(i >= 0 && (i+1 < lines.size())) {
				g.setColor(borderColor[borderColorIndex]);
				drawTextHorizontal(g, String.valueOf(firstNorthing-(i+1)*1000), x ,y);
			}
		}
	}

	private void arrayCopy(int[] dst, int... src) {
		for (int i = 0; i < src.length; i++) 
			dst[i] = src[i];
	}
}

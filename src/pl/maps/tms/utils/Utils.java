package pl.maps.tms.utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import pl.maps.tms.TileGridProvider;

public final class Utils {
	private Utils() {}
	
	public static String fillTileURLTemplate(String template, int x, int y, int z) {
		template = template.replaceAll(Pattern.quote("${z}"), String.valueOf(z));
		template = template.replaceAll(Pattern.quote("${x}"), String.valueOf(x));
		template = template.replaceAll(Pattern.quote("${y}"), String.valueOf(y));
		return template;
	}

	public static Image createWaitingImage(TileGridProvider provider) {
		Image image;
		Dimension size = provider.getTileSize();
		image = new BufferedImage(size.width, size.height, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, size.width, size.height);
		g.setColor(Color.BLACK);
		int width = Math.max(2,size.width/10);
		g.setStroke(new BasicStroke(width));
		g.drawRect(0,0, size.width, size.height);
		g.drawLine(0,0,size.width, size.height);
		g.drawLine(size.width, 0, 0, size.height);
		return image;
	}
	public static void showError(Component parentComponent, Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		JOptionPane.showMessageDialog(parentComponent, sw.toString());
	}

}

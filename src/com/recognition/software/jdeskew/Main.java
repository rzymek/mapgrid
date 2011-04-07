package com.recognition.software.jdeskew;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/*
 * JDeskew 1.0
 * 
 * http://www.recognition-software.com/image/deskew/
 * 
 * A Java port of the VB.NET class provided by GMSE Imaging.
 * 
 * http://www.codeproject.com/KB/graphics/Deskew_an_Image.aspx
 *  
 * License Type: The Code Project Open License (CPOL) 1.02
 * 
 * @author Roland Quast
 */

public class Main {

    private static final double MINIMUM_DESKEW_THRESHOLD = 0.05d;

    /**
     * @param args
     */
    public static void main(String[] args) {
	
	if ( args == null || args.length != 2 ) {
	    System.out.println("Usage: java -jar jdeskew.jar input.jpg output.jpg");
	    System.exit(0);
	}
	
	try {
	    new Main(args);
	    System.out.println("Deskew Success.");
	} catch (Exception ex) {
	    System.out.println("Deskew Failure.");
	    ex.printStackTrace();
	}
    }

    private String[] commandLineArgs;

    private BufferedImage outputImage;

    public Main(String[] args) throws IOException {
	setCommandLineArgs(args);
	performDeskew();
	saveOutput();
    }

    public String[] getCommandLineArgs() {
	return commandLineArgs;
    }

    public BufferedImage getOutputImage() {
	return outputImage;
    }

    private BufferedImage getSourceImage() throws IOException {
	return ImageUtil.readImageFile(new File(getCommandLineArgs()[0]));
    }

    private void performDeskew() throws IOException {

	BufferedImage sourceImage = getSourceImage();
	BufferedImage outputImage = null;

	ImageDeskew deskew = new ImageDeskew(sourceImage);
	double imageSkewAngle = deskew.getSkewAngle();

	if ((imageSkewAngle > Main.MINIMUM_DESKEW_THRESHOLD || imageSkewAngle < -(Main.MINIMUM_DESKEW_THRESHOLD))) {
	    outputImage = ImageUtil.rotate(sourceImage, -imageSkewAngle,
		    sourceImage.getWidth() / 2, sourceImage
		    .getHeight() / 2);
	} else {
	    outputImage = sourceImage;
	}

	setOutputImage(outputImage);

    }

    private void saveOutput() throws IOException {
	
	File outputFileName = new File(getCommandLineArgs()[1]);
	String[] nameParts = outputFileName.getName().split("\\.");
	String extension = nameParts[(nameParts.length - 1)];
	
	ImageIO.write(getOutputImage(), extension.toUpperCase().trim(), outputFileName);
	
    }

    public void setCommandLineArgs(String[] commandLineArgs) {
	this.commandLineArgs = commandLineArgs;
    }


    public void setOutputImage(BufferedImage outputImage) {
	this.outputImage = outputImage;
    }

}

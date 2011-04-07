
package HNF;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * Klasse zum Zeichnen von Eingangs-und Ausgangsbild.
 * In dieser Klasse werden auch Hin-und Ruecktranformation
 * durchgefuehrt und im Bild-bzw.Houghraum farblich angezeigt.
 * 
 * @author Rene Iser, Behrang Karimibabak, Simon Winkelbach
 *
 */
public class GeneralHoughViewer extends JPanel{

    ImageWindow input,output;
    boolean transform=false,backTransform=false,findMax=false;
    int xT,yT,r,numberOfMax;
    double[][] maxArray;
    int[][] maxPosArray;
    double t;
    
    GeneralHoughViewer(ImageWindow input,ImageWindow output){
        this.input=input;
        this.output=output;
    }
    
    public void paintComponent(Graphics g){
        input.paintComponent(g);
        output.paintComponent(g);
        if(transform) paintTransformedPoint(g);
        if(backTransform) paintBackTransformedPoint(g);
        if(findMax)paintMaxima(g);
        
    }
    
    /**
     * Transformiert einen Punkt in den Houghraum und
     * stellt die Tranformation farblich dar.
     * @param g
     */
    public void paintTransformedPoint(Graphics g){
        g.setColor(Color.red);
    	int sizeX = 256;
    	int sizeY = 256;

    	int r,rMax,rangeofTheta;

    	int thetaMax;
    	int thetaMin;
    	double theta;

    	rMax = (int)(Math.sqrt((sizeX/2*sizeX/2) +(sizeY/2*sizeY/2)));///2);
        thetaMin = 0;
        thetaMax = 360;

    	rangeofTheta = 360;
			for(int t=0; t<rangeofTheta; t+=1)
			{
				theta = Math.toRadians(t);

				r = (int)((((double)xT-((double)sizeX/2.0)) * Math.cos(theta)) + (((double)yT-((double)sizeY/2.0)) * Math.sin(theta)));

					r = -(r-rMax);
					if(!(r<0) && !(r>rMax))
					{
						g.drawLine(t+326,r+20,t+326,r+20);
					}


			}

    }
    
    /**
     *Transformiert einen Punkt im Houghraum zurueck in
     *den Bildraum und markiert die Gerade im
     *Bildraum farblich.
     */
    
    public void paintBackTransformedPoint(Graphics g){
        g.setColor(Color.green);
    	int xPixel,yPixel;

    	for(int x = 0; x< 256; x++)
    	{

    		yPixel = (int)(((double)r/Math.sin(t)) - (((double)x-128.0)*(Math.cos(t)/Math.sin(t)))+128.0);

    		// To get a Clear line....we draw in x- as well as in y direction
    		xPixel = (int)(((double)r/Math.cos(t)) - (((double)x-128.0)*Math.tan(t))+128.0);

    		if(x>0 && x<256 && yPixel>0 && yPixel<256)
    		{
    			g.drawLine(x+20 ,yPixel+20,x+20 ,yPixel+20);
    		}

    		if(xPixel>0 && xPixel<256)
    		{
    			g.drawLine(xPixel+20, x+20,xPixel+20, x+20);
    		}
    	}


        
    }
    
    /**
     * Findet Maxima im Houghraum
     * und transformiert diese zurueck.
     * Die Positionen der Maxima stehen
     * in dem Array maxArray.
     * @see setMaxPosArray(int[][] max)
     * @param g
     */
    public void paintMaxima(Graphics g){
        
    	int xPixel,yPixel;
    	
    	for(int i=0;i<numberOfMax;i++){
    	    t=maxArray[i][0];
    	    r=(int)maxArray[i][1];
    	    g.setColor(Color.green);
    	    g.drawRect(maxPosArray[i][0]+321,maxPosArray[i][1]+15,10,10);
    	    g.setColor(Color.yellow);
    	   	for(int x = 0; x< 256; x++)
        	{

        		yPixel = (int)(((double)r/Math.sin(t)) - (((double)x-128.0)*(Math.cos(t)/Math.sin(t)))+128.0);

        		// To get a Clear line....we draw in x- as well as in y direction
        		xPixel = (int)(((double)r/Math.cos(t)) - (((double)x-128.0)*Math.tan(t))+128.0);

        		if(x>0 && x<256 && yPixel>0 && yPixel<256)
        		{
        			g.drawLine(x+20 ,yPixel+20,x+20 ,yPixel+20);
        		}

        		if(xPixel>0 && xPixel<256)
        		{
        			g.drawLine(xPixel+20, x+20,xPixel+20, x+20);
        		}
        	}

    	}
        
    }
    
    /**
     *  
     * Ist transform gleich 'true' wird die
     * Transformation des aktullen Punkts im Bildraum
     * als rote Kurve im Houghraum markiert.
     * --> this.transform=T;
     * 
     * @param T gibt an,ob die Transformation angezeigt werden soll
     */
     
    public void setTransform(boolean T){
        this.transform=T;
    }
    
    /**
     * Ist backTransform gleich 'true' wird der aktuelle
     * Punkt im Houghraum in den Bildraum zuruecktransformiert.
     * Die Gerade wird gelb im Bildraum angezeigt.
     * --> this.backTransform=bT;
     * @param bT gibt an,ob die Ruecktransformation angezeigt werden soll
     */
    
    public void setBackTransform(boolean bT){
        this.backTransform=bT;
    }
    
    /**
     * In dieser Methode werden die Koordination fuer die 
     * Transformation gesetzt.
     * @param x x-Koordinate des zu transformierenden Punktes
     * @param y y-Koordinate des zu transformierenden Punktes
     */
    
    public void setTransformCoordinates(int x,int y){
        this.xT=x;
        this.yT=y;
    }
    
    /**
     * In dieser Methode werden die Koordination fuer die 
     * Ruecktransformation gesetzt.
     * @param r r-Koordinate des zu transformierenden Punktes
     * @param t theta-Koordinate des zu transformierenden Punktes
     */
    
    public void setBackTransformCoordinates(int r,double t){
        this.t=t;
        this.r=r;
    }
    
    /**
     *Hier wird festgelegt,wie viele Maxima angezeigt
     *werden sollen.
     *@param max Anzahl der Maxima. 
     */
    
    public void setNumberOfMax(int max){
        this.numberOfMax=max;
    }
    
    /**
     * Dieser Methode wird das Array mit den Positionen der Maxima 
     * uebergeben. Hierbei ist maxArray[i][0] jeweils
     * der Winkel im Bogenma� und maxArray[i][1] 
     * der Abstand der potentiellen Geraden vom Ursprung.
     * (--> der Houghraum hat das Koordinatensystem (theta,r))
     * 
     * @param max Array mit den Maximumpositionen
     */
    
    public void setMaxArray(double[][] max){
        this.maxArray=max;
    }
    
    
    /**
     * Hier werden die Positionen der Maxima in Bild- 
     * koordinaten angegeben,damit die Maxima auf der Oberflaeche
     * farblich markiert werden koennen.
     * @param max die Positionen der Maxima in Bildoordinaten
     */
    public void setMaxPosArray(int[][] max){
        this.maxPosArray=max;
    }
    
    /**
     * Ist findMax auf 'true' gesetzt werden die Maxima in dem 
     * Array maxArray angezeigt.
     * @param findMax gibt an,ob die Maxima angezeigt werden sollen
     */
    public void setFindMax(boolean findMax){
        this.findMax=findMax;
    }
    
}


package HNF;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.*;

/**
 * Dieses Applet veranschaulicht die Houghtransformation in 
 * Hessischer Normalform. Es besteht die Moeglichkeit zur Auswahl
 * verschiedener Bilder,sowie eines frei-Hand-Modus. Ausserdem kann 
 * eine vorher ausgewaehlte Anzahl von Maxima im Houghraum zurueck-
 * transformiert werden.
 * Bei der Houghtransformation in HNF wird von der Formel
 * x*sin(theta)+ y*cos(theta) ausgegangen. Soll also ein Punkt (x,y)
 * im Bildraum transformiert werden,so wird dieser auf eine Sinus-
 * foermige Kurve im Houghraum abgebildet(0<=theta<=360).Umgekehrt liegt
 * ein Punkt im Houghraum auf einer Geraden im Bildraum. Wird also ein
 * Punkt(theta,r) zuruecktransformiert,entsteht eine Gerade.
 * Wird die Maus ueber das Eingangsbild bewegt,so wird der Punkt gleich
 * transformiert und als rote Kurve im Houghraum angezeigt. 
 * Befindet sich umgekehrt die Maus ueber dem Houghraum,wird die korres-
 * pondierende Gerade gruen im Bildraum angezeigt.
 * 
 * @author Rene Iser, Behrang Karimibabak, Simon Winkelbach
 *
 */

public class GeneralHoughTransformation extends JApplet implements MouseInputListener {
    protected iRPImage inputImage,outputImage,inputCopy;
    protected ImageWindow input,output;
    protected JPanel operationPanel,dialogPanel,messagePanel;
    protected JDialog pictureDialog;
    protected GeneralHoughViewer viewer;
    protected JButton reset;
    protected JButton calculateMax,choosePicture,shapeButton;
    protected JButton pointButton,triangelButton,chaosButton,emptyButton;
    protected JButton[] pictureButtons;
    protected JLabel message1,message2;
    protected int picSize=256;
    protected double[][] maxArray=new double[10][2];
    protected int[][] maxPosArray=new int[10][2];
    protected final int xFromLeft=20;
    protected final int yFromTop=20;
    protected final int betweenThePics=10;
    protected int rangeofTheta,rMax,numberOfMax;
    protected boolean empty=true,copyInput=true,findMax=false,updateMaxCoordinates=false;
    protected final int ITresh=0;
    protected String[] pictures={"shape","points","triangel","caos","Empty"};
    protected String[] max={"1","2","3","4","5","6","7","8","9"};
    protected ImageIcon dialogIcon;
    protected String plaf,imageToLoad;
    protected DialogImageProvider imgProv;
    protected Image[] dialogImg;
    protected String[] dialogImgNames;
    
    public void init() {
        numberOfMax=1;
 
        Image dialogImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource("dialogIcon.gif"));
        dialogIcon=new ImageIcon(dialogImage);
        
        imgProv = new DialogImageProvider();
        dialogImg = imgProv.getImages();
        dialogImgNames = imgProv.getImageNames();
        
        this.initializeImages();
        
        rMax=(int)Math.sqrt(inputImage.sizeX/2*inputImage.sizeX/2+inputImage.sizeY/2*inputImage.sizeY/2);

        viewer = new GeneralHoughViewer(input,output);

        this.initializeButtons();
        this.initializeLabels();
        
        pictureButtons=new JButton[dialogImg.length];
        String buttonName;
        
        /*
         *Hier werden die Buttons zur Bildauswahl initialisiert.
         *In 'buttonName' wird der Name des zu ladenen Bildes gespeichert.
         */
        for(int i=0;i<dialogImg.length;i++){
                        
            buttonName=dialogImgNames[i];
            pictureButtons[i]= new JButton(new ImageIcon(dialogImg[i]));
            pictureButtons[i].setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            pictureButtons[i].setName(buttonName);
             
        }
        
        JOptionPane picturePane=new JOptionPane();
        picturePane.setOptions(pictureButtons);
        picturePane.setMessage("");
        
        pictureDialog=picturePane.createDialog(this,"select input image");
        
        
        messagePanel=new JPanel();
        messagePanel.setPreferredSize(new Dimension(220,45));
        messagePanel.setBorder(BorderFactory.createEtchedBorder());
        messagePanel.setLayout(new FlowLayout(FlowLayout.LEFT,3,3));
        messagePanel.add(message1);
        messagePanel.add(message2);
        
        Dimension d = new Dimension(this.getWidth(),60);
        operationPanel=new JPanel();
        operationPanel.setPreferredSize(d);
        operationPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        operationPanel.setLayout(new FlowLayout(FlowLayout.LEFT,10,8));
        operationPanel.add(choosePicture); 
        operationPanel.add(calculateMax);
        operationPanel.add(reset);
        operationPanel.add(messagePanel);
        
        getContentPane().add("North",operationPanel);
        getContentPane().add(viewer);
        
 
        this.initializeActionListeners(); 
        this.changeLookAndFeel();
        

             
             addMouseListener(this);
             addMouseMotionListener(this);
        
        
    }
    
    /**
     * Konfiguration der Statusleiste
     * im Startzustand.
     *
     */
    
    public void initializeLabels(){
 
        message1=new JLabel("Select a new image or press button");
        message2=new JLabel("with the red quadrat to find maxima");
        
    }
    
    /**
     * Erstellen der Buttons.
     * Alle Buttons werden mit Icons versehen und 
     * bekommen eine Umrandung.
     */
    
    public void initializeButtons(){
        Image clearImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource("reset.gif"));
        reset=new JButton(new ImageIcon(clearImage));
        reset.setToolTipText("resets images");
        reset.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        
        Image maxImage=Toolkit.getDefaultToolkit().getImage(getClass().getResource("max.gif"));
        calculateMax=new JButton(new ImageIcon(maxImage));
        calculateMax.setToolTipText("This button calculates maxima in Hough space");
        calculateMax.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        
        Image pictureImage=Toolkit.getDefaultToolkit().getImage(getClass().getResource("picture.gif"));
        choosePicture=new JButton(new ImageIcon(pictureImage));
        choosePicture.setToolTipText("choose picture");
        choosePicture.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        

    }
    
    
    /**
     * Konfiguration von Eingangsbild und
     * Houghraum.
     */
    
    public void initializeImages(){
        inputImage=new iRPImage();
        inputImage.LoadImage(dialogImgNames[0]);
        input=new ImageWindow(inputImage,20,20);
        input.setOriginInTheMiddle(true);
        input.setTitle("input image");
        input.setCoordinateSystemVariable("x","y");
        
        
        outputImage=GeneralTransform(inputImage);
        output=new ImageWindow(outputImage,20+inputImage.sizeX+50,20);
        output.setTitle("Hough space");
        output.setOriginInTheMiddle(false);
        output.setCoordinateSystemVariable("theta","r");
    }
    
    /**
     * Mit Hilfe der ActionListener werden beim
     * Klicken auf die Buttons die entsprechenden Methoden
     * aufgerufen,z.B. wird beim druecken auf den choosePicture-button
     * die Dialogbox mit der Bildauswahl geoeffnet.
     */
    
    public void initializeActionListeners(){
 
        
        calculateMax.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                getMax();
  
            }
        });
        
        
        
        reset.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                reset();  
            }
        });
        
        choosePicture.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                 pictureDialog.show();
            }
        });
     
         
        for (int i=0;i<dialogImg.length;i++){
            pictureButtons[i].addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    imageToLoad=((JButton)e.getSource()).getName();
                    changePicture(imageToLoad);
                    pictureDialog.setVisible(false);
                }
            });     	
        }
        
        
    }
    
    
    /**
     * Aenderung der Optik
     *
     */
    
    public void changeLookAndFeel(){
        try{
        	plaf = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";

        	UIManager.setLookAndFeel(plaf);
        	SwingUtilities.updateComponentTreeUI(this);
        	SwingUtilities.updateComponentTreeUI(operationPanel);
        	SwingUtilities.updateComponentTreeUI(choosePicture);
        	SwingUtilities.updateComponentTreeUI(calculateMax);
         	SwingUtilities.updateComponentTreeUI(reset);
       		for (int i=0;i<pictureButtons.length;i++){
    		    SwingUtilities.updateComponentTreeUI(pictureButtons[i]);
    		}
         	
 
             } catch (UnsupportedLookAndFeelException e) {
               System.err.println(e.toString());
             } catch (ClassNotFoundException e) {
               System.err.println(e.toString());
             } catch (InstantiationException e) {
               System.err.println(e.toString());
             } catch (IllegalAccessException e) {
               System.err.println(e.toString());
             }
    }
    
    /**
     * Wechsel des Eingangsbildes."Input0" stellt den
     * frei-Hand-Modus dar.Das Bild wird automatisch nach
     * Auswahl komplett transformiert.
     * @param pic Name des Bildes
     */
    public void changePicture(String pic){
        
        
        inputImage.LoadImage(pic);

        if(pic.equals("Input0.gif")){
    
            empty=true;
        }
        else empty=false;
    
        if(empty){
            message1.setText("Keep mouse button pressed and");
            message2.setText("drag it over the image space");
        }
        else{
            message1.setText("Select a new image or press button");
            message2.setText("with the red quadrat to find maxima");
        }
        
        
        input.changeImage(inputImage);
        outputImage=GeneralTransform(inputImage);
        outputImage.takeTheLogarithm();
        output.changeImage(outputImage);
        copyInput=true;
        viewer.setTransform(false);
        viewer.setFindMax(false);
        repaint();
    }
    
    /**
     * Eventuelle Manipulationen im
     * Bild-/Houghraum werden rueckgaengig gemacht. 
     */
    
    public void reset(){
        if(empty){
            inputImage=new iRPImage(picSize,picSize);
            input.changeImage(inputImage);
            outputImage=GeneralTransform(inputImage);
            output.changeImage(outputImage);
            copyInput=true;
            repaint();
        }
        else{
            input.changeImage(inputImage);
            output.changeImage(outputImage);
            copyInput=true;
            repaint();
        }
        
        viewer.setFindMax(false);
    }
    
    public void getMax(){
        String maxima=(String)JOptionPane.showInputDialog(this,"Number of Maxima","Maxima",
                JOptionPane.QUESTION_MESSAGE,dialogIcon,
                max,max[0]);
       
        if(maxima !=null) {
            numberOfMax=Integer.parseInt(maxima);
            viewer.setFindMax(true);
            viewer.setNumberOfMax(numberOfMax);
            calcMax();
        }
        
    }
    
    /**
     * Findet die Maxima. Zuerst wird das Bild des
     * Houghraumes kopiert,da dieses nicht direkt manipuliert
     * werden soll. Die Maxima werden in dem Array
     * maxArray gespeichert und dem viewer uebergeben,damit
     * dieser die Ruecktransformation anzeigen kann.
     * 
     */
    
    public void calcMax() {
        iRPImage Max; 
        Max=outputImage.copy();
        inputCopy=inputImage.copy();
        int x,y,r;
        int maxPosition;
        double theta;
        int sizeX=Max.sizeX;
        int sizeY=Max.sizeY;
        int xPos,yPos;
        for (int i=0;i<numberOfMax;i++){
            maxPosition=Max.GetMax();
            x= maxPosition % Max.sizeX;
            y = (maxPosition-x)/Max.sizeX;
            maxPosArray[i][0]=x;
            maxPosArray[i][1]=y;
            if(Max.GetPixel(maxPosition)>0){

                for (int k=x-10;k<=x+10;k++){
                    for(int j=y-10;j<=y+10;j++){
                        xPos=k;yPos=j;
                        if(k<0)xPos=xPos+sizeX;
                        else if(k>=sizeX)xPos=xPos%sizeX;
                        if(j<0) yPos=yPos+sizeY;
                        else if(j>=sizeY)yPos=yPos%sizeY;
                        Max.SetPixel(xPos,yPos,0);
                        
                    }
                }
 
         		r = (y-rMax)*-1;
        		theta = Math.toRadians(x);
        		maxArray[i][0]=theta;
        		maxArray[i][1]=r;
                            
            }
        }
        viewer.setMaxArray(maxArray);
        viewer.setMaxPosArray(maxPosArray);
         repaint();
 
    }
    
 

    
    /**
     * Methode zur Houghtransformation in Hessischer Normalform
     * @param T Das zu transformierende Bild.
     */
    public iRPImage GeneralTransform(iRPImage T)
    {
    	int sizeX = T.GetSizeX();
    	int sizeY = T.GetSizeY();

    	int r;

    	int thetaMax;
    	int thetaMin;
    	double theta;

    	rMax = (int)(Math.sqrt((sizeX/2*sizeX/2) +(sizeY/2*sizeY/2)));///2);
        thetaMin = 0;
        thetaMax = 360;

    	rangeofTheta = 360;



    	iRPImage R = new iRPImage(rangeofTheta+1,(rMax+1));

    	for(int x=0; x<sizeX ; x++)
    	{
    		for(int y=0; y<sizeY; y++)
    		{

    			if(T.GetPixel(x,y) > ITresh)
    			{
    				for(int t=0; t<rangeofTheta; t+=1)
    				{
    					theta = Math.toRadians(t);

    					r = (int)((((double)x-((double)sizeX/2.0)) * Math.cos(theta)) + (((double)y-((double)sizeY/2.0)) * Math.sin(theta)));

    						r = -(r-rMax);
    						if(!(r<0) && !(r>rMax))
    						{
    							R.SetPixel(t,r,R.GetPixel(t,r)+1);
    						}


    				}

    			}
    		}

    	}


    	return R;
    }
    
    /**
     * Gibt 'true' zurueck,falls die Maus ueber dem Eingangsbild
     * ist,sonst 'false'.
     * @param x
     * @param y
     */
    
    public boolean mouseOverInput(int x,int y){
        if(x>=20 && x<276 && y>=80 && y<336) return true;
        else return false;
        
    }
    
    /**
     * Gibt 'true' zurueck,falls die Maus ueber dem Ausgangsbild
     * ist,sonst 'false'.
     * @param x
     * @param y
     */
    
    public boolean mouseOverOutput(int x,int y){
        if(x>=326 && x<326+outputImage.sizeX && y>=80 && y<80+outputImage.sizeY) return true;
        else return false;
    }
    
	/**
	 * Die Methode setzt abhaengig von der Mausposition
	 * die Grauwerte und die Mauskoordinaten fuer die Anzeige
	 * unterhalb des Bildes neu. Die Koordinaten werden
	 * dem viewer auch zur Hin-bzw. zur Ruecktransformation 
	 * uebergeben.
	 */
    
    public void mouseMoved(MouseEvent e) {
        int xPos=e.getX();
        int yPos=e.getY();
        int value;
        if(viewer.transform==true){
            viewer.setTransform(false);
            repaint();
        }
        if(viewer.backTransform==true){
            viewer.setBackTransform(false);
            repaint();
        }
        
 
        if(mouseOverInput(xPos,yPos)){
            setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            value=inputImage.GetPixel(xPos-20,yPos-80);
            input.updateMousePosition(xPos-20,yPos-80,value);
            viewer.setTransform(true);
            viewer.setBackTransform(false);
            viewer.setTransformCoordinates(xPos-20,yPos-80);
            repaint();
        }
        else if(mouseOverOutput(xPos,yPos)){
            setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            int r;
            double theta;
            viewer.setTransform(false);
            viewer.setBackTransform(true);
            xPos=xPos-326;
            yPos=yPos-80;
            r=(yPos-rMax)*-1;
            theta=Math.toRadians(xPos);
            viewer.setBackTransformCoordinates(r,theta);
            value=outputImage.GetPixel(xPos,yPos);
            output.updateMousePosition(xPos,yPos,value);
            repaint();
        }
        else setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
             
    }
    
    /**
     * Ist die Maus ueber dem Eingangsbild und ist
     * empty gleich 'true' wird der Punkt transformiert.
     */
    
    public void mousePressed(MouseEvent e) {
        int xPos=e.getX();
        int yPos=e.getY();
 
        
        if(mouseOverInput(xPos,yPos) && empty){
            viewer.setTransform(false);
            inputImage.SetPixel(xPos-20,yPos-80,255);
            outputImage=GeneralTransform(inputImage);
            output.changeImage(outputImage);
            repaint();
        }
        
 
        
    }
    
    /**
     * Ist der frei-Hand-Modus aktiviert(empty==true)
     * so wird der Punkt in den Houghraum transformiert,wenn die
     * Maus ueber dem Eingangsbild ist.
     */
    
    public void mouseDragged(MouseEvent e) {
       int xPos=e.getX();
       int yPos=e.getY();
       if(mouseOverInput(xPos,yPos) && empty){
           viewer.setTransform(false);
           inputImage.SetPixel(xPos-20,yPos-80,255);
           outputImage=GeneralTransform(inputImage);
           output.changeImage(outputImage);
           repaint();
       }
       
        
    }

    /**
     * Die Anzeige der Maxima wird im viewer
     * deaktiviert.
     */
    public void mouseClicked(MouseEvent e) {
        viewer.setFindMax(false);
        
    }

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}
        
 
    public void mouseReleased(MouseEvent e) {}
        

}



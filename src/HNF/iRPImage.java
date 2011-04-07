package HNF;
/**
 * This class is a custom class used for storing and manipulating grayscale pictures.
 * The user is enabled to load, change and get information about the specified Image
 * using the different methods of the class. The class is the main class for visualising
 * different image processing algorithms in java.
 *
 * @author bka
 * @version "0.1"
 */
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;


public class iRPImage extends Canvas{

protected int sizeX;
protected int sizeY;
protected int image[];
protected boolean lgImage=false,logNow=false;
protected int[] logImage;




/**
 * Constructs an iRPImage with size 0,0 and an initializes the
 * Image[] Array with 0 elements
 */
  public iRPImage(){
	sizeX = 0;
	sizeY = 0;
	image = new int[0];
	
  }

/**
 * Constructs an iRPImage given width and height of the Image
 *
 * @param 	width	the width of the iRPImage
 * @param	height	the height of the iRPImage
 */
  public iRPImage(int width, int height){
	  sizeX = width;
	  sizeY = height;
	  image = new int[sizeX * sizeY];
	  logImage=new int[sizeX*sizeY];
	  

  }

  public void Clear(int v)
  {
  	int i;
  	for (i=0;i<sizeX*sizeY;i++) image[i]=v;
}

/**
 * Sets the size of the iRPImage to the desired size x and y
 *
 * @param	x	the width of the iRPImage
 * @param   y	the height of the iRPImage
 */
  public void SetSize(int x, int y){
	sizeX = x;
	sizeY = y;
	image = new int[x*y];
  }

/**
 * Returns the width of the iRPImage
 *
 * @return	the width of the iRPImage
 */
  int GetSizeX(){
 	return sizeX;
  }

/**
 * Returns the height of the iRPImage
 *
 * @return	the height of the iRPImage
 */
  int GetSizeY(){
 	return sizeY;
  }


/**
 * Returns the minimum element of the image[] array
 *
 * @return	the minimum element of the iRPImage
 */
  public int GetMin(){

   int position = 0;
   int min = image[0];

   for(int i = 1; i< sizeX*sizeY ; i++)
   {
   		if(image[i]< min)
   		{
   		  min = image[i];
   		  position = i;
   		}
   }

   return position;
  }



/**
 * Returns the maximum element of the image[] array
 *
 * @return	the maximum element of the iRPImage
 */

  public int GetMax(){

    int position = 0;
    int max = image[0];

    for(int i = 1; i< sizeX*sizeY ; i++)
    {
		if(image[i]>max)
		{
			max = image[i];
			position = i;
		}
	}
	return position;
  }


/**
 * Returns the arrayvalue of the iRPImage on the place (x,y)
 *
 * @param	x	x position of the pixel
 * @param 	y	y position of the pixel
 * @return 	the value of image at position (x,y)
 */
  public int GetPixel(int x, int y){
	return image[x + y * sizeX];
  }

/**
 * Returns the value of the image[] array on position i
 *
 * @param	i	the specified position
 * @return	value of image[] at position i
 */
  public int GetPixel(int i){
 	return image[i];
  }


/**
 * Sets the value of image[] at position (x,y) to a desired value
 *
 * @param	x	x-position
 * @param 	y	y-position
 * @param 	value	the desired value
 */
 public void SetPixel(int x, int y, int value){
	image[x + y * sizeX] = value;
  }

/**
 * Sets the value of image[] at position i to a desired value
 *
 * @param	i	position i
 * @param 	value	the desired value
 */
 public void SetPixel(int i,int value){
	  image[i]=value;
  }


/**
* Makes a doubleiRPImage from an iRPImage
*
* @return the doubleiRPImage of this iRPImage
*/

 public doubleiRPImage makedoubleiRPImage()
  {
    doubleiRPImage A = new doubleiRPImage(sizeX, sizeY);
	for(int i=0; i<sizeX ; i++)
  	{
  		for(int j=0; j<sizeY; j++)
  		{
  			A.SetPixel(i,j,(double)this.GetPixel(i,j));
  		}
  	}
	return A;
  }

 
 /*
  * Zieht eine Linie von (a1,b1) nach (a2,b2) und setzt den jeweiligen
  * Pixelwert auf 'value'
  */ 
 
public void drawLineWithConstantValue(int a1,int b1,int a2,int b2,int value){
  	double a,b,delta;

	//System.out.println("("+a1+","+b1+") ("+a2+","+b2);
  	if(Math.abs(b2-b1)<Math.abs(a2-a1))
  	{
		if(a1>a2) delta=-1;
		else delta=1;
  		for(a = a1; a != a2+delta; a+=delta)//for(a = a1; a < a2; a++)
    	{
			b = (double)b1 + ((double)(b2-b1)* ((double)a-(double)a1)/((double)a2-(double)a1));
			//Die Abfrage ist zuf�llig wegen ArrayindexofBoundexception!!
			if(((int)b != sizeY) && ((int)a !=sizeX))
			{  
				SetPixel((int)a,(int)b, value);
			}
		}
	}
  	else
  	{

		if (b1>b2) delta=-1;
		else delta=1;
     	for(b = b1; b != b2+delta; b+=delta)
     	{
			a =(double)a1 + ((double)(a2-a1)* ((double)b-(double)b1)/((double)b2-(double)b1));
			if((int)b != sizeY && ((int)a != sizeX))
			{  
				SetPixel((int)a , (int)b , value);
			}
   		}
  	}
}


/**
 * Draws a line between two given points (a1,b1) and (a2,b2)
 *
 * @param	a1	x-position of the first point
 * @param 	b1	y-position of the first point
 * @param 	a2	x-position of the second point
 * @param 	b2	y-position of the second point
 */
  public void drawLine(int a1, int b1, int a2, int b2)
  {

  	double a,b,delta;

	//System.out.println("("+a1+","+b1+") ("+a2+","+b2);
  	if(Math.abs(b2-b1)<Math.abs(a2-a1))
  	{
		if(a1>a2) delta=-1;
		else delta=1;
  		for(a = a1; a != a2+delta; a+=delta)//for(a = a1; a < a2; a++)
    	{
			b = (double)b1 + ((double)(b2-b1)* ((double)a-(double)a1)/((double)a2-(double)a1));
			//Die Abfrage ist zuf�llig wegen ArrayindexofBoundexception!!
			if(((int)b != sizeY) && ((int)a !=sizeX))
			{
				SetPixel((int)a,(int)b, this.GetPixel((int)a,(int)b)+1 );
			}
		}
	}
  	else
  	{

		if (b1>b2) delta=-1;
		else delta=1;
     	for(b = b1; b != b2+delta; b+=delta)
     	{
			a =(double)a1 + ((double)(a2-a1)* ((double)b-(double)b1)/((double)b2-(double)b1));
			if((int)b != sizeY && ((int)a != sizeX))
			{
				SetPixel((int)a , (int)b , this.GetPixel((int)a, (int)b)+1 );
			}
   		}
  	}
}



/**
 * Normalizes a given array to values between 0 and 255
 */
  protected void Normalize(){

  	int min, max;

  	min = GetPixel(GetMin());
  	max = GetPixel(GetMax());

	if (max!=min)
	{
  		for(int i = 0 ; i < sizeX*sizeY; i++)
  			image[i]=((image[i]-min)*255)/(max-min);
	}
	else {

	    for (int i=0;i<sizeX*sizeY;i++)
	        image[i]=0;
	    
	}
  }
  
  public iRPImage copy()
  {
    iRPImage G = new iRPImage(this.GetSizeX(), this.GetSizeY());
  	for(int i =0; i< sizeX * sizeY ; i++)
  	{
  		G.image[i] = image[i];
  	}
  	return G;
  }



/**
 * Loads an Image (.jpg and .gif) given a filename
 *
 * @param	FileName	Name of the file to load as String
 */
  public void LoadImage(String FileName){

	  Image pic;
      MediaTracker media = new MediaTracker(this);
      pic = Toolkit.getDefaultToolkit().getImage(getClass().getResource(FileName));
      media.addImage(pic, 0);
      try {
        media.waitForID(0);
        }
      catch (Exception e) {}
      sizeX = pic.getWidth(this);
      sizeY = pic.getHeight(this);

      image = new int[sizeX*sizeY];
      logImage=new int[sizeX*sizeY];

      PixelGrabber pg = new PixelGrabber(pic, 0, 0, sizeX, sizeY, image, 0, sizeX);

	  try { pg.grabPixels(); } catch(InterruptedException e) {}

	  //Convert color to grey values:
      for (int i=0;i<sizeX*sizeY;i++)
      {
		  image[i] = (
		  			   ( (image[i] >> 16) & 0xff)   //red
		  			 + ( (image[i] >> 8) & 0xff)  //green
		  			 + ( image[i] & 0xff)		  //blue
		  			 )/3;
	  }
  }
  
  public void setLogImage(boolean log){
      this.lgImage=log;
  }
  
  public void takeTheLogarithm(){
      for(int i=0;i<sizeX*sizeY;i++){
          logImage[i]=(int)Math.round((Math.log(image[i])/Math.log(2))+1);
      }
       
  }




/**
 * Draws the iRPImage to the screen, given a point (x,y) where
 * (x,y) specifies the upper left corner position of the picture.
 * This method normalizes the image[] array and creates an Image from
 * it using the MemoryImageSource-class.
 *
 * @param	g	the Graohics context of the screen
 * @param 	x	x-position to draw from (upper left corner)
 * @param 	y	y-position to draw from (upper left corner)
 */
  public void PaintImage(Graphics g, int x, int y){
	Image OutPut;

	
	iRPImage col_image= new iRPImage(sizeX,sizeY);
	for (int i=0; i<sizeX*sizeY; i++) {
	    if(lgImage){
	        col_image.SetPixel(i,logImage[i]); 		    

	    }
	    else{
	        col_image.SetPixel(i,image[i]);
	    }
	    

	}

	col_image.Normalize();

	//Convert grey values to color:
	for (int i=0;i<sizeX*sizeY;i++)
	{
			  col_image.SetPixel(i,
			  			   ( (col_image.GetPixel(i) & 0xff) << 16) //red
			  			 + ( (col_image.GetPixel(i)& 0xff) << 8)  //green
			  			 + ( col_image.GetPixel(i) & 0xff)		  //blue
			  			 + 0xff000000);				  //alpha
	}

	MemoryImageSource mis = new MemoryImageSource(sizeX, sizeY, col_image.image, 0, sizeX);
	OutPut = createImage(mis);

    MediaTracker mt = new MediaTracker(this);
	mt.addImage(OutPut, 0);
	try { mt.waitForAll(); } catch (InterruptedException e) {}

    g.drawImage(OutPut , x , y, this);
  }



}
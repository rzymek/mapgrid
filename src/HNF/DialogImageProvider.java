
package HNF;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;

/**
 * Diese Klasse dient dazu, ein Verzeichnis nach Bilddateien
 * zu durchsuchen. Hierbei wird davon ausgegangen, dass die Bilder
 * die Namen Input0.(jpg/gif), Input1.(jpg/gif), ..., Input9.(jpg/gif)
 * haben.
 * @author Rene Iser, Simon Winkelbach
 */


public class DialogImageProvider {

    Image[] img;
    String[] imgNames;
    File[] entries;
    File dir;
    int numberOfImages=0;
    
    /**
     * Diese Methode speichert alle Bildateien einem Array mit
     * Image-Objekten und gibt dieses zurueck.
     * 
     * @return img das Array mit den Image-Objekten
     */
    public Image[] getImages() 
    {
        imgNames = new String[10];
        int count;
        
        for (count=0; count<10; count++)
        {
            imgNames[count]="Input";
            imgNames[count]+=String.valueOf(count);
            imgNames[count]+=".gif";
            System.out.println(getClass().getResource(imgNames[count])); 
            if (getClass().getResource(imgNames[count])==null) 
            {
				imgNames[count]="Input";
				imgNames[count]+=String.valueOf(count);
				imgNames[count]+=".jpg";
				System.out.println(getClass().getResource(imgNames[count])); 
                if (getClass().getResource(imgNames[count])==null) break;
            }
        }
        
        img = new Image[count];
        
        for (int i=0;i<count;i++)
        {
			Image input=Toolkit.getDefaultToolkit().getImage(getClass().getResource(imgNames[i]));
            img[i]=input.getScaledInstance(64,64,input.SCALE_SMOOTH);
        }
        
        return img;
    }
    
    /**
     * Gibt ein Stringarray mit den Dateinamen zurueck
     * @return imgNames das Array mit den Dateinamen
     */
    
    public String[] getImageNames()
    {
        return imgNames;
    }
 
}

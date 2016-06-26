import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.ArrayList;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;

public class GraphicsLab extends JPanel
{
    
    public GraphicsLab()
    {}
    public static BufferedImage AlphaMask(BufferedImage source, BufferedImage mask)
    {
        ImageFilter filter = new RGBImageFilter()
        {
            public final int filterRGB(int x, int y, int rgb)
            {
                return (rgb << 8) & 0xFF000000;
            }
        };
    
        ImageProducer ip = new FilteredImageSource(mask.getSource(), filter);
        Image alphamask =  Toolkit.getDefaultToolkit().createImage(ip);
        
        BufferedImage dest = new BufferedImage(
                source.getWidth(), source.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
                
        Graphics2D g2 = dest.createGraphics();
        g2.drawImage(source, 0, 0, null);
        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.DST_IN, 1.0F);
        g2.setComposite(ac);
        g2.drawImage(alphamask, 0, 0, null);
        g2.dispose();
        return dest;
    }
    public static BufferedImage scale(BufferedImage source, int xshf, int yshf, int xsize, int ysize )
   {
        BufferedImage scaledImage = new BufferedImage(
                xsize, ysize, 
                BufferedImage.TYPE_INT_ARGB);
                
        Graphics2D g2 = scaledImage.createGraphics();
        //g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
        //    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(source, xshf, yshf, xsize, ysize, null);
        //g2.drawImage(source, 100, 200, 200, 200, null);
        g2.dispose();
        return scaledImage;
   }
    public static BufferedImage crop(BufferedImage src, Rectangle rect)
   {
       BufferedImage dest = new BufferedImage((int)rect.getWidth(), (int)rect.getHeight(), BufferedImage.TYPE_INT_ARGB);
       Graphics g = dest.getGraphics();
       g.setColor(Color.black);
       g.fillRect(0,0,(int)rect.getWidth(),(int)rect.getHeight());
       g.drawImage(src, 0, 0, (int)rect.getWidth(), (int)rect.getHeight(), (int)rect.getX(), (int)rect.getY(), (int)rect.getX() + (int)rect.getWidth(), (int)rect.getY() +(int) rect.getHeight(), null);
       g.dispose();
       return dest;
   }
   public static BufferedImage copy(BufferedImage pic)
   {
       BufferedImage dest = new BufferedImage((int)pic.getWidth(), (int)pic.getHeight(), BufferedImage.TYPE_INT_ARGB);
       Graphics g = dest.getGraphics();
       g.drawImage(pic, 0, 0, null);
       g.dispose();
       return dest;
       
   }
   public static void writeImage(BufferedImage image,String filename, boolean type)
   {
       //typer: true for image, false for mask
       String file;
       if(type)
       {
           file = filename;
       }
       else
       {
           file = filename+"_alpha";
       }
       try{
           File outputfile = new File("pucks/"+file+".png");
           ImageIO.write(image,"png", outputfile);
        } catch(IOException e1){
            e1.printStackTrace();
        }
    }
}

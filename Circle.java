import java.awt.*;
//added image stuff
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

public class Circle 
{
    private double  x, y;
    private int radius;
    private Color color;
    private double circumfrence =2;
    double pi = 3.1416;
    //create an image for the ball
    BufferedImage image, mask, skin, scaled, mod_image, mod_mask;
    private String face;
    private boolean use_face;
    
    //image dimensions after scaling 
    private int height, width;
    private double My_ratio;
    
    double template_ratio;
 
    //the variable ratio should later be fixed and not passed in, just a consideration
    //ratio is picture edge divided by inner circle diameter
    public Circle (int size, Color shade, double CenterX, double CenterY, double ratio, String name)
    {
        radius = size;
        color = shade;
        My_ratio = ratio;
        x = CenterX;
        y = CenterY;
        face = name;
        Skin();
        use_face = true;
        
        template_ratio = (double) 290/230;
        //template_ratio = 1;
        
        int scale = (int)(size*2*ratio*template_ratio);
        height = scale;
        width =  scale;
        
        mod_image = Scale(image,scale);
        mod_mask = Scale(mask,scale);
        skin = AlphaMask(mod_image,mod_mask);
        
        
    }
     public Circle (int size, Color shade, double CenterX, double CenterY)
    {
        radius = size;
        color = shade;
        x = CenterX;
        y = CenterY;
        use_face = false;
        
    }
    public void Skin() {
        try {
           image = ImageIO.read(new File("textures/pucks/"+face+".png"));
           mask =  ImageIO.read(new File("textures/pucks/"+face+"_alpha.png"));
       } catch (IOException e) {
       }
    }
    public BufferedImage Scale(BufferedImage source, int dimension)
    {
        BufferedImage scaledImage = new BufferedImage(
                //source.getWidth(), source.getHeight(), 
                width, height, 
                BufferedImage.TYPE_INT_ARGB);
                
        Graphics2D g2 = scaledImage.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(source, 0, 0, dimension, dimension, null);
        g2.dispose();
        return scaledImage;
    }
    public BufferedImage AlphaMask(BufferedImage source, BufferedImage mask)
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
    public void draw( Graphics page)
    {
        if(!use_face) {
            page.setColor( color);
            page.fillOval((int)(x-radius),(int)(y-radius),radius*2,radius*2);
        }
        else {
            page.drawImage(skin,(int)(x-width/2),(int)(y-height/2),null);
            //page.drawOval((int)(x-radius),(int)(y-radius),radius*2,radius*2);
        }
    }
    public void Face(String name)
    {
        face = name;
        Skin();
        int scale = (int)(radius*2*My_ratio*template_ratio);
        mod_image = Scale(image,scale);
        mod_mask = Scale(mask,scale);
        skin = AlphaMask(mod_image,mod_mask);
    }
    public void setRatio(double ratio)
    {
        //Skin();
        My_ratio = ratio;
        int scale = (int)(radius*2*ratio*template_ratio);
        height = scale;
        width = scale;
        
        mod_image = Scale(image,scale);
        mod_mask = Scale(mask,scale);
        skin = AlphaMask(mod_image,mod_mask);
        
    }
    public String getName()
    {
        return face;
    }
    public void setRadius (int size)
    {
        radius = size;
    }
    public void setColor( Color shade)
    {
        color = shade;
    }
    public void setX (double upperX)
    {
        x = upperX;
    }
    public void setY(double upperY)
    {
        y = upperY;
    }
    public int getRadius()
    {
        return radius;
    }
    public Color getColor()
    {
        return color;
    }
    public double getX()
    {
        return x;
    }
    public double getY()
    {
        return y;
    }
}

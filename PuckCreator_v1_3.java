//********************************************************************
//  Rebound.java
//  Demonstrates an animation and the use of the Timer class.
//********************************************************************

//import java.applet.Applet;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.ArrayList;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;
import java.util.Scanner;

public class PuckCreator_v1_3 extends JPanel
{
   private static int WIDTH = 320;
   private static int HEIGHT = 568;

   private final int DELAY = 10;
   private javax.swing.Timer timer;
 
   private static BufferedImage picture, modpicture, img,  display_pic, alpha, template_img, template_mask, template,
   frame_img, frame_mask, frame, bottom_panel_fit, bottom_panel_draw, bottom_panel_acc, top_panel_fit, top_panel_draw, top_panel_make, top_panel_name, keyboard ;
   
   private static int xpic,ypic;
   
   private static double xpos,ypos;
   int max_xpos, min_xpos, max_ypos, min_ypos;
   int top_offset = 270;
   int bottom_offset = 270;
   int left_offset = 145;
   int right_offset = 145;
   int min_zoom = 240;
   int max_xzoom = 3*WIDTH;
   int max_yzoom = 3*HEIGHT;
   
   
   int curr_xpic, curr_ypic;
   
   int x_offset;
   int y_offset;
   private static double ratio;
   
   private Rectangle crop_window;
   
   private static int stage;
   private static boolean crop = false;
  
   private static int start_x,start_y,end_x,end_y;
   
   private Point point = new Point(0,0);

   private int menu, display_time, step;
   
   private static int[] xpoints;
   private static int[] ypoints;
   private static int[] xdraw;
   private static int[] ydraw;
   
   private static int[] polysub1_x;
   private static int[] polysub1_y;
   private static int[] polysub2_x;
   private static int[] polysub2_y;
   //used for debugging
   private static int[] polysub3_x;
   private static int[] polysub3_y;
   
   private boolean generated = false;
   private static int gen_points = 0;
   private static int points = 1000;
   
   //for naming the file
   private static String puckname;
   
  
   
   //-----------------------------------------------------------------
   //  Sets up the applet, including the timer for the animation.
   //-----------------------------------------------------------------
   /*
   public static void main (String[] args)
    {
        JFrame frame = new JFrame ("Puck Creator");
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new PuckCreator_v1_3(args[0]) );
        
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    */
   public PuckCreator_v1_3(BufferedImage image, String filename)
   {
      
      setFocusable(true);
      
      puckname = filename;
      
      stage = 0;
      
      xpoints = new int[points];
      ypoints = new int[points];
      xdraw = new int[points];
      ydraw = new int[points];
      
      ratio = 1;
     
      picture = image;
      
      try{
          //picture = ImageIO.read(new File("gallery/picture_"+puckname+".jpg"));
          template_img = ImageIO.read(new File("textures/puck_template.jpg"));
          template_mask = ImageIO.read(new File("textures/puck_template_alpha.jpg"));
          
          frame_img = ImageIO.read(new File("textures/frame_template.jpg"));
          frame_mask = ImageIO.read(new File("textures/frame_template_alpha.jpg"));
          
          bottom_panel_fit = ImageIO.read(new File("textures/bottom_panel_fit.jpg"));
          bottom_panel_draw = ImageIO.read(new File("textures/bottom_panel_draw.jpg"));
          bottom_panel_acc = ImageIO.read(new File("textures/bottom_panel_acc.jpg"));
          
          top_panel_fit = ImageIO.read(new File("textures/top_panel_fit.jpg"));
          top_panel_draw = ImageIO.read(new File("textures/top_panel_draw.jpg"));
          top_panel_make = ImageIO.read(new File("textures/top_panel_make.jpg"));
          top_panel_name = ImageIO.read(new File("textures/top_panel_name.jpg"));
          
          keyboard = ImageIO.read(new File("textures/keyboard_screen.jpg"));
          
      } catch (IOException e) {
      }
      
      
      template = AlphaMask(template_img,template_mask);
      frame = AlphaMask(frame_img,frame_mask);

      double xdim = picture.getWidth();
      double ydim = picture.getHeight();
   
      double screen_ratio = (double)WIDTH/(double)HEIGHT;
      double pic_ratio = xdim/ydim;
      
     // y wall fit
     if(pic_ratio >= screen_ratio)
     {
        ypic = HEIGHT;
        xpic = (int)(pic_ratio*HEIGHT);
        xpos = -(xpic-WIDTH)/2;
        ypos = 0;
     }
     else // x wall fit
     {
        xpic = WIDTH;
        ypic = (int)( WIDTH/pic_ratio);
        xpos = 0;
        ypos = -(ypic-568)/2;
     }
     
     
     picture  = CreatePic(picture,0,0,(int)(xpic*ratio),(int)(ypic*ratio));
     modpicture = picture;
     
     
     set_panlimits();
     
     
     
   }
   
   //-----------------------------------------------------------------
   //  Draws the image in the current location.
   //-----------------------------------------------------------------
   public void paintComponent (Graphics page)
   {
        
             //page.drawImage(modpicture, (int)xpos, (int)ypos, null);
             
             if(stage == 0)
             {
                 page.drawImage(modpicture, (int)xpos, (int)ypos, null);
                 page.drawImage(template,0,0,null);
                 page.drawImage(top_panel_fit,0,0,null);
                 page.drawImage(bottom_panel_fit,0,HEIGHT-60,null);
                 
             }
             else if(stage == 1)
             {
                 page.drawImage(modpicture, (int)xpos, (int)ypos, null);
                 page.drawImage(frame,0,0,null);
                 drawRegion(page);
                 page.drawImage(top_panel_draw,0,0,null);
                 page.drawImage(bottom_panel_draw,0,HEIGHT-60,null);
                 
             }
             else if(stage == 2)
             {
                  
                 if(!crop)
                 {
                     BufferedImage temp_pic = copy(modpicture);
                     
                     temp_pic = drawPolyRegion(temp_pic);
                     display_pic = copy(temp_pic);
                     crop_window = new Rectangle(15-(int)xpos,139-(int)ypos,290,290);
                     img = crop(temp_pic,crop_window);
                     
                     temp_pic = drawAlphaface(temp_pic);
                     alpha = crop(temp_pic,crop_window);
                     
                     crop = true;
                     //modpicture = temp_pic;
                 }
                 page.drawImage(display_pic, (int)xpos, (int)ypos, null);
                 page.drawImage(top_panel_make,0,0,null);
                 page.drawImage(bottom_panel_acc,0,HEIGHT-60,null);
             }
             else if(stage == 3)
             {
                 page.drawImage(display_pic, (int)xpos, (int)ypos-80, null);
                 page.drawImage(keyboard, 0, HEIGHT-260, null);
                 page.drawImage(top_panel_name,0,0,null);
                 page.setColor(Color.black);
                 page.drawString(puckname,20,HEIGHT-230);
             }
             
   }
   public void set_panlimits()
   {
       min_xpos = (WIDTH-right_offset)-modpicture.getWidth();
       max_xpos = left_offset;
     
       if(min_xpos > max_xpos)
       {
           max_xpos = min_xpos;
           min_xpos = left_offset;
       }
       
       min_ypos = (HEIGHT-bottom_offset)-modpicture.getHeight();
       max_ypos = top_offset;
     
       if(min_ypos > max_ypos)
       {
           max_ypos = min_ypos;
           min_ypos = top_offset;
       }
       
   }
   public double xlimit_distance(double x_point)
   {
       
       if( x_point < min_xpos)
       {
           return (min_xpos - x_point);
       }
       else if(x_point > max_xpos)
       {
           return (x_point - max_xpos);
       }
       else
       {
           return 0;
       }
            
   }
   public double ylimit_distance(double y_point)
   {
       
       if( y_point < min_ypos)
       {
           return (min_ypos - y_point);
       }
       else if(y_point > max_ypos)
       {
           return (y_point - max_ypos);
       }
       else
       {
           return 0;
       }
            
    }
   public static void SaveFiles()
   {
       writeImage(img,puckname,true);
       writeImage(alpha,puckname,false);  
   }
   public void drawRegion(Graphics g)
   {
       Graphics2D g2 = (Graphics2D)g;
       final float dash1[] = {10.0f};
       final BasicStroke dashed = new BasicStroke(5.0f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND,10.0f, dash1, 0.0f);
       g2.setStroke(dashed);
       
       //green dashed style
       g2.setColor(new Color(0,200,0));
       
       g2.drawPolyline(xdraw,ydraw,gen_points);
       
   }
   public static BufferedImage drawPolyRegion(BufferedImage pic)
   {
       
       BufferedImage dest = new BufferedImage((int)pic.getWidth(), (int)pic.getHeight(), BufferedImage.TYPE_INT_ARGB);
       
       Graphics g = dest.getGraphics();
       
       g.drawImage(pic, 0, 0, null);
       g.setColor(Color.black);
       //g.setColor(Color.blue);
       //g.fillOval(top_x-10,top_y-10,20,20);
       g.fillPolygon(polysub1_x,polysub1_y,polysub1_x.length);
       //g.drawPolyline(polysub1_x,polysub1_y,polysub1_x.length);
       //g.setColor(Color.red);
       //g.fillOval(bottom_x-10,bottom_y-10,20,20);
       g.fillPolygon(polysub2_x,polysub2_y,polysub2_x.length);
       //g.drawPolyline(polysub2_x,polysub2_y,polysub2_x.length);
       
       g.dispose();
       
       return dest;
   }
   public BufferedImage drawAlphaface(BufferedImage pic)
   {
       
       BufferedImage dest = new BufferedImage((int)pic.getWidth(), (int)pic.getHeight(), BufferedImage.TYPE_INT_ARGB);
       Graphics g = dest.getGraphics();
       
       g.drawImage(pic, 0, 0, null);
       g.setColor(Color.white);
       g.fillPolygon(xpoints,ypoints,gen_points);
       g.dispose();
       
       return dest;
   }  
   public BufferedImage crop(BufferedImage src, Rectangle rect)
   {
       BufferedImage dest = new BufferedImage((int)rect.getWidth(), (int)rect.getHeight(), BufferedImage.TYPE_INT_ARGB);
       Graphics g = dest.getGraphics();
       g.setColor(Color.black);
       g.fillRect(0,0,(int)rect.getWidth(),(int)rect.getHeight());
       g.drawImage(src, 0, 0, (int)rect.getWidth(), (int)rect.getHeight(), (int)rect.getX(), (int)rect.getY(), (int)rect.getX() + (int)rect.getWidth(), (int)rect.getY() +(int) rect.getHeight(), null);
       g.dispose();
       return dest;
   }
   public BufferedImage copy(BufferedImage pic)
   {
       BufferedImage dest = new BufferedImage((int)pic.getWidth(), (int)pic.getHeight(), BufferedImage.TYPE_INT_ARGB);
       Graphics g = dest.getGraphics();
       g.drawImage(pic, 0, 0, null);
       g.dispose();
       return dest;
       
   }
   public BufferedImage CreatePic(BufferedImage source, int xshf, int yshf, int xsize, int ysize )
   {
        BufferedImage scaledImage = new BufferedImage(
                xpic, ypic, 
                BufferedImage.TYPE_INT_ARGB);
                
        Graphics2D g2 = scaledImage.createGraphics();
        //g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
        //    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(source, xshf, yshf, xsize, ysize, null);
        //g2.drawImage(source, 100, 200, 200, 200, null);
        g2.dispose();
        return scaledImage;
   }
   public static BufferedImage ModPic(BufferedImage source, int xshf, int yshf, int xsize, int ysize )
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
           File outputfile = new File("textures/pucks/"+file+".png");
           ImageIO.write(image,"png", outputfile);
        } catch(IOException e1){
            e1.printStackTrace();
        }
    }
    public static int MaxIndex(int[] points, int length)
    {
        int max = -1;
        int max_index = 0;
        for(int i = 0; i < length; i++)
        {
            
            if(points[i] > max)
            {
                max = points[i];
                max_index = i;
            }
            
        }
        return max_index;
    }
    public static int MinIndex(int[] points, int length)
    {
        int min = 999;
        int min_index = 0;
        for(int i = 0; i < length; i++)
        {
            if(points[i] < min)
            {
                min = points[i];
                min_index = i;
            }
        }
        return min_index;
    }
    public static void PolyRegions()
    {
        
        int bottom_index = MaxIndex(ypoints,gen_points);
        int top_index = MinIndex(ypoints,gen_points);
        //debug test to show points
        int top_y = ypoints[top_index];
        int top_x = xpoints[top_index];
        
        int bottom_y = ypoints[bottom_index];
        int bottom_x = xpoints[bottom_index];
        
        
        int sub1_length = Math.abs(top_index - bottom_index);
        
        polysub1_x = new int[sub1_length+5];
        polysub1_y = new int[sub1_length+5];
        
        int start_index;
        if(top_index < bottom_index)
        {
            start_index = top_index;
        }
        else
        {
            start_index = bottom_index;
        }
        System.arraycopy(xpoints,start_index,polysub1_x,0,sub1_length);
        System.arraycopy(ypoints,start_index,polysub1_y,0,sub1_length);
        
        int sub2_length = gen_points-sub1_length;
        
        polysub2_x = new int[sub2_length+5];
        polysub2_y = new int[sub2_length+5];
       
        System.arraycopy(xpoints,start_index+sub1_length,polysub2_x,0,sub2_length-start_index);
        System.arraycopy(xpoints,0,polysub2_x,sub2_length-start_index,start_index);
        
        System.arraycopy(ypoints,start_index+sub1_length,polysub2_y,0,sub2_length-start_index);
        System.arraycopy(ypoints,0,polysub2_y,sub2_length-start_index,start_index);
        
        int max_x1 = polysub1_x[MaxIndex(polysub1_x,sub1_length)];
        int max_x2 = polysub2_x[MaxIndex(polysub2_x,sub2_length)];
        
        //sub group 1 is right half
        if(max_x1 > max_x2)
        {
            //CW rotation
            
            if(MaxIndex(polysub1_y,sub1_length) > MinIndex(polysub1_y,sub1_length))
            {
                
                //add border points based on position information
                
                //sub1
                //connect to sub2
                polysub1_x[sub1_length] = polysub2_x[0];
                polysub1_y[sub1_length] = polysub2_y[0];
                //add bottom mid point
                polysub1_x[sub1_length+1] = bottom_x;
                polysub1_y[sub1_length+1] = HEIGHT-(int)ypos;
                //add bottom right point
                polysub1_x[sub1_length+2] = WIDTH-(int)xpos;
                polysub1_y[sub1_length+2] = HEIGHT-(int)ypos;
                //add top right point
                polysub1_x[sub1_length+3] = WIDTH-(int)xpos;
                polysub1_y[sub1_length+3] = -(int)ypos;
                //add top mid point
                polysub1_x[sub1_length+4] = top_x;
                polysub1_y[sub1_length+4] = -(int)ypos;
                
                
                //sub2
                //connect to sub1
                polysub2_x[sub2_length] = polysub1_x[0];
                polysub2_y[sub2_length] = polysub1_y[0];
                //add top mid point
                polysub2_x[sub2_length+1] = top_x;
                polysub2_y[sub2_length+1] = -(int)ypos;
                //add top left point
                polysub2_x[sub2_length+2] = -(int)xpos;
                polysub2_y[sub2_length+2] = -(int)ypos;
                //add bottom left point
                polysub2_x[sub2_length+3] = -(int)xpos;
                polysub2_y[sub2_length+3] = HEIGHT-(int)ypos;
                //add bottom mid point
                polysub2_x[sub2_length+4] = bottom_x;
                polysub2_y[sub2_length+4] = HEIGHT-(int)ypos;
                //*/
            }
            //CCW
            else
            {
                
                //add border points based on position information
                
                //sub1
                //connect to sub2
                polysub1_x[sub1_length] = polysub2_x[0];
                polysub1_y[sub1_length] = polysub2_y[0];
                //add top mid point
                polysub1_x[sub1_length+1] = top_x;
                polysub1_y[sub1_length+1] = -(int)ypos;
                //add top right point
                polysub1_x[sub1_length+2] = WIDTH-(int)xpos;
                polysub1_y[sub1_length+2] = -(int)ypos;
                //add bottom right point
                polysub1_x[sub1_length+3] = WIDTH-(int)xpos;
                polysub1_y[sub1_length+3] = HEIGHT-(int)ypos;
                //add bottom mid point
                polysub1_x[sub1_length+4] = bottom_x;
                polysub1_y[sub1_length+4] = HEIGHT-(int)ypos;
                
                //sub2
                //connect to sub1
                polysub2_x[sub2_length] = polysub1_x[0];
                polysub2_y[sub2_length] = polysub1_y[0];
                //add bottom mid point
                polysub2_x[sub2_length+1] = bottom_x;
                polysub2_y[sub2_length+1] = HEIGHT-(int)ypos;
                //add bottom left point
                polysub2_x[sub2_length+2] = -(int)xpos;
                polysub2_y[sub2_length+2] = HEIGHT-(int)ypos;
                //add top left point
                polysub2_x[sub2_length+3] = -(int)xpos;
                polysub2_y[sub2_length+3] = -(int)ypos;
                //add top mid point
                polysub2_x[sub2_length+4] =  top_x;
                polysub2_y[sub2_length+4] = -(int)ypos;
                //*/
            }
        }
        //sub group 1 is left half
        else
        {
            
           //CW rotation
            if(MaxIndex(polysub1_y,sub1_length) < MinIndex(polysub1_y,sub1_length))
            {
                
                //add border points based on position information
                
                //sub1
                //connect to sub2
                polysub1_x[sub1_length] = polysub2_x[0];
                polysub1_y[sub1_length] = polysub2_y[0];
                //add top mid point
                polysub1_x[sub1_length+1] = top_x;
                polysub1_y[sub1_length+1] = -(int)ypos;
                //add top left point
                polysub1_x[sub1_length+2] = -(int)xpos;
                polysub1_y[sub1_length+2] = -(int)ypos;
                //add bottom left point
                polysub1_x[sub1_length+3] = -(int)xpos;
                polysub1_y[sub1_length+3] = HEIGHT-(int)ypos;
                //add bottom mid point
                polysub1_x[sub1_length+4] = bottom_x;
                polysub1_y[sub1_length+4] = HEIGHT-(int)ypos;
                
                
                //sub2
                //connect to sub1
                polysub2_x[sub2_length] = polysub1_x[0];
                polysub2_y[sub2_length] = polysub1_y[0];
                //add bottom mid point
                polysub2_x[sub2_length+1] = bottom_x;
                polysub2_y[sub2_length+1] = HEIGHT-(int)ypos;
                //add bottom right point
                polysub2_x[sub2_length+2] = WIDTH-(int)xpos;
                polysub2_y[sub2_length+2] = HEIGHT-(int)ypos;
                //add top right point
                polysub2_x[sub2_length+3] = WIDTH-(int)xpos;
                polysub2_y[sub2_length+3] = -(int)ypos;
                //add top mid point
                polysub2_x[sub2_length+4] = top_x;
                polysub2_y[sub2_length+4] = -(int)ypos;
                
            }
            //CCW
            else
            {
                
                //add border points based on position information
                
                //sub1
                //connect to sub2
                polysub1_x[sub1_length] = polysub2_x[0];
                polysub1_y[sub1_length] = polysub2_y[0];
                //add botom mid point
                polysub1_x[sub1_length+1] = bottom_x;
                polysub1_y[sub1_length+1] = HEIGHT-(int)ypos;
                //add bottom left point
                polysub1_x[sub1_length+2] = -(int)xpos;
                polysub1_y[sub1_length+2] = HEIGHT-(int)ypos;
                //add top left point
                polysub1_x[sub1_length+3] = -(int)xpos;
                polysub1_y[sub1_length+3] = -(int)ypos;
                //add top mid point
                polysub1_x[sub1_length+4] = top_x;
                polysub1_y[sub1_length+4] = -(int)ypos;
                
                //sub2
                //connect to sub1
                polysub2_x[sub2_length] = polysub1_x[0];
                polysub2_y[sub2_length] = polysub1_y[0];
                //add top mid point
                polysub2_x[sub2_length+1] = top_x;
                polysub2_y[sub2_length+1] = -(int)ypos;
                //add top right point
                polysub2_x[sub2_length+2] = WIDTH-(int)xpos;
                polysub2_y[sub2_length+2] = -(int)ypos;
                //add bottom right point
                polysub2_x[sub2_length+3] = WIDTH-(int)xpos;
                polysub2_y[sub2_length+3] = HEIGHT-(int)ypos;
                //add bottom mid point
                polysub2_x[sub2_length+4] = bottom_x;
                polysub2_y[sub2_length+4] = HEIGHT-(int)ypos;
                
            } 
            
        }
            
    }
    // call to progress from one stage to another
    public static void Progress()
    {
       if(stage == 0)
       {
           stage = 1;
       }
       else if(stage == 1)
       {
           PolyRegions();
           stage = 2;
       } 
       else if(stage ==2 )
       {
           stage = 3;
       }
       else if(stage ==3 )
       {
           stage = 4;
       }
    }
    public static void Previous()
    {
       if(stage == 0)
       {
           stage = 0;
       }
       else
       {
           ClearPoints();
           crop = false;
           stage--;
       } 
        
    }
    public static int getStage()
    {
        return stage;
    }
    public void setPuckName(String input)
    {
        puckname = input;
    }
    public static void ClearPoints()
    {
      
        xpoints = new int[points];
        ypoints = new int[points];
        xdraw = new int[points];
        ydraw = new int[points];
        
        gen_points = 0;
    }
    public static void CleanUp()
    {
        ClearPoints();
        crop = false;
    }  
    // sets the control for performing zoom operations in the picture
    // sets is the relative movement of the zoom button
    public  void Zoom(int steps)
    {
       double new_ratio = ratio -.02*steps;
       //ratio -= .02*steps;
       
       int pre_width = modpicture.getWidth();
       int pre_height = modpicture.getHeight();
       
       int  new_xpic = (int)(xpic*new_ratio);
       int new_ypic = (int)(ypic*new_ratio);
       
       //System.out.println(steps);
       
       if( (new_xpic >= min_zoom && new_ypic >= min_zoom) && (new_xpic <= max_xzoom && new_ypic <= max_yzoom) )
       {
           modpicture  = ModPic(picture,0,0,new_xpic,new_ypic);
         
           set_panlimits();
           
           int post_width = modpicture.getWidth();
           int post_height = modpicture.getHeight();
           
           int x_focus = WIDTH/2;
           int y_focus = HEIGHT/2;
           
           double xdim = (double)post_width/pre_width;
           double ydim = (double)post_height/pre_height;
           
           xpos = (xdim*(xpos-x_focus))+x_focus;
           ypos = (ydim*(ypos-y_focus))+y_focus;
           
           ratio = new_ratio;
       }
    }
    // set start cordinate for performing panning movements
    public static void MousePoint(int x_point, int y_point)
    {
        if(stage == 0)
         {
             start_x = x_point;
             start_y = y_point;
             
         }
         
    }
    // sets the control for panning and drawing
    public void MouseControl(int x_point, int y_point)
    {
        if(stage == 0)
        {
            end_x = x_point;
            end_y = y_point;
            
            int diff_x = end_x - start_x;
            int diff_y = end_y - start_y;
            
            double new_xpos = xpos + diff_x;
            double new_ypos = ypos + diff_y;
            
            if(xlimit_distance(xpos) >=  xlimit_distance(new_xpos) )
            {
                xpos = new_xpos;
            }
            if(ylimit_distance(ypos) >=  ylimit_distance(new_ypos) )
            {
                ypos = new_ypos;
            }
            
            start_x = end_x;
            start_y = end_y;
            
            
            
        }
        else if(stage == 1 && gen_points < points)
        {
            double distance;
            int pix_res = 2;
            //if point is not in the top or bottom panels
            if(y_point < HEIGHT-60 && y_point > 60)
            {
                if(gen_points == 0)
                {
                    xpoints[gen_points] = x_point-(int)xpos;
                    xdraw[gen_points] = x_point;
                    
                    ypoints[gen_points] = y_point-(int)ypos;
                    ydraw[gen_points] = y_point;
                    
                    gen_points++;
                    distance = 0;
                }
                else
                {
                    int pre_x = xpoints[gen_points-1];
                    int pre_y = ypoints[gen_points-1];
                
                    int curr_x = x_point-(int)xpos;
                    int curr_y = y_point-(int)ypos;
                
                    int diff_x = curr_x-pre_x;
                    int diff_y = curr_y-pre_y;
                
                    distance = Math.sqrt(diff_x*diff_x+diff_y*diff_y);
                    
                }
                if(distance >= pix_res)
                {
                    xpoints[gen_points] = x_point-(int)xpos;
                    xdraw[gen_points] = x_point;
                    
                    ypoints[gen_points] = y_point-(int)ypos;
                    ydraw[gen_points] = y_point;
                    
                    gen_points++;
                }
            }
        }
         
    }
}
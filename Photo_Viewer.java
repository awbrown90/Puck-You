//********************************************************************
//  Rebound.java
//  Demonstrates an animation and the use of the Timer class.
//********************************************************************

//import java.applet.Applet;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.ArrayList;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class Photo_Viewer extends JPanel
{
   private final int WIDTH = 320;
   private final int HEIGHT = 568;

   private final int DELAY = 10;
   private javax.swing.Timer timer;
  
   //Map<Integer,BufferedImage> photos = new HashMap<Integer,BufferedImage>();
   ArrayList<BufferedImage> photos = new ArrayList<BufferedImage>();
   ArrayList<BufferedImage> Modphotos = new ArrayList<BufferedImage>();
   
   private int num_photos;
   private int num_files;
   
   private Rectangle crop_window;
   
 
   private BufferedImage load_screen, picture, modpicture, img, alpha, template_img, template_mask, template,
   frame_img, frame_mask, frame, top_panel;
   
   int xpic,ypic;
   
   double xpos,ypos;
   
   
   int x_offset, y_offset, y_scroll, yscroll_min, yscroll_max;
   double ratio = 1;
  
   int stage = 0;
   boolean crop = false;
  
   int start_x,start_y,end_x,end_y;
   
   private Point point = new Point(0,0);

   private int menu, display_time, step;
   
   private int[] xpoints;
   private int[] ypoints;
   private int[] xdraw;
   private int[] ydraw;
   
   private int[] polysub1_x;
   private int[] polysub1_y;
   private int[] polysub2_x;
   private int[] polysub2_y;
   
   private boolean generated = false;
   private int gen_points = 0;
   private int points = 1000;
   
   private String puckname;
   
   //Botton dimensions and positions
   
   //TOP PANEL
   int panel_width = WIDTH;
   int panel_height = 60; //pixels
   
   //BACK
   int back_width = 88; //pixels
   int back_height= 48;//pixels
   
   int back_xoffset = 5;//pixels
   int back_yoffset = 5;//pixels
   
   DisplayButton BACK = new DisplayButton(back_width,back_height,back_xoffset,back_yoffset);
   
   //NEXT
   int next_width = 102; //pixels
   int next_height= 48;//pixels
   
   int next_xoffset = 211;//pixels
   int next_yoffset = HEIGHT-6-next_height;//pixels
   
   DisplayButton NEXT = new DisplayButton(next_width,next_height,next_xoffset,next_yoffset);
   
   //RESET
   int reset_width = 72; //pixels
   int reset_height= 48;//pixels
   
   int reset_xoffset = 133;//pixels
   int reset_yoffset = HEIGHT-6-reset_height;//pixels
   
   DisplayButton RESET = new DisplayButton(reset_width,reset_height,reset_xoffset,reset_yoffset);
   
   //ACCEPT
   int acc_width = 124; //pixels
   int acc_height= 48;//pixels
   
   int acc_xoffset = 188;//pixels
   int acc_yoffset = HEIGHT-6-acc_height;//pixels
   
   DisplayButton ACCEPT = new DisplayButton(acc_width,acc_height,acc_xoffset,acc_yoffset);
   
   PuckCreator_v1_3 PuckCreator;
   
   UI_Keyboard keyboard = new UI_Keyboard();
   
   boolean loaded;
   Font loadfont = new Font("BankGothic Md BT", Font.PLAIN, 25);
   //-----------------------------------------------------------------
   //  Sets up the applet, including the timer for the animation.
   //-----------------------------------------------------------------
   
   public Photo_Viewer(ArrayList<BufferedImage> cached_photos, ArrayList<BufferedImage> cached_modphotos)
   {
     
      x_offset = WIDTH/4;
      y_offset = x_offset;
      
      y_scroll = 0;
      yscroll_max = 0;
      
      num_photos = 0;
      
      try{
                   
             load_screen = ImageIO.read(new File("textures/loading.jpg"));
             top_panel = ImageIO.read(new File("textures/top_panel.jpg"));
        
            } catch (IOException e){
      }
      
      loaded =false;
      
      if(cached_photos == null)
      {
          loaded =false;
          worker.execute();
      }
      else
      {
          loaded =true;
          photos = cached_photos;
          Modphotos = cached_modphotos;
          num_photos = photos.size();
          
          int mod_ex = 1;
          if(num_photos%4 == 0)
          {
              mod_ex = 0;
          }
          
          yscroll_min = HEIGHT - (y_offset*(num_photos/4+mod_ex)+y_scroll+panel_height) ;
          
          if( yscroll_min > 0)
          {
              yscroll_min = 0;
          }
 
      }
      
      //loading = false;
      
      menu = 0;
     
   }
   //load all the images in the gallery folder
   public void loadImages()
   {
     File folder = new File("textures/gallery");
     File[] listOfFiles = folder.listFiles();
     num_files = listOfFiles.length;
     
     for (int i = 0; i < num_files; i++) {
          if (listOfFiles[i].isFile() ) {
            String file_name = listOfFiles[i].getName();
            int name_length = file_name.length();
            String file_ext = file_name.substring(name_length-4,name_length);
            if(file_ext.equals(".jpg")||file_ext.equals(".JPG")||file_ext.equals("jpeg")||file_ext.equals(".png"))
            {
                //System.out.println("File " + file_name);
                BufferedImage picture;
                
                try{
                   
                    picture = ImageIO.read(listOfFiles[i]);
                    //picture = ImageIO.read(new File("gallery/"+file_name));
                    //picture = ImageIO.read(new BufferedInputStream(new FileInputStream(listOfFiles[i])));
                    
                    
                    photos.add(picture);
                    picture = fitImage(picture);
                    Modphotos.add(picture);
                    num_photos++;
                    
                } catch (IOException e){
                }
                
            }
          } 
     }
     

   }
   public ArrayList<BufferedImage> getPhotos()
   {
       return photos;
   }
   public ArrayList<BufferedImage> getModphotos()
   {
       return Modphotos;
   }
   public void ClearPuck()
   {
       PuckCreator = null;
   }
   //fit viewing size for photo
   public BufferedImage fitImage(BufferedImage pic)
   {
      //BufferedImage pic
      double xdim = pic.getWidth();
      double ydim = pic.getHeight();
   
      double screen_ratio = (double)WIDTH/(double)HEIGHT;
      double pic_ratio = xdim/ydim;
      
     // horizontal wall fit
     if(pic_ratio >= 1)
     {
        ypic = WIDTH;
        xpic = (int)(pic_ratio*ypic);
        xpos = -(xpic-WIDTH)/2;
        ypos = (HEIGHT-ypic)/2;
        crop_window = new Rectangle((int)-xpos,0,WIDTH,WIDTH);
        
     }
     else // vertical wall fit
     {
        xpic = WIDTH;
        ypic = (int)(xpic/pic_ratio);
        xpos = 0;
        ypos = (HEIGHT-ypic)/2;
        crop_window = new Rectangle(0,(int)(ypic-WIDTH)/2,WIDTH,WIDTH);
        
     }
     
     pic  = CreatePic(pic,0,0,(int)(xpic),(int)(ypic));
     pic= crop(pic,crop_window);
     xpic = pic.getWidth();
     ypic = pic.getHeight();
     pic  = ModPic(pic,0,0,(int)(xpic*.25),(int)(ypic*.25));
     xpic = pic.getWidth();
     ypic = pic.getHeight();
     xpos = -(xpic-WIDTH)/2;
     ypos =  (HEIGHT-ypic)/2; 
     
     return pic;
       
   }
   //crop_window = new Rectangle(15-(int)xpos,139-(int)ypos,290,290);
   //img = crop(modpicture,crop_window);
   
   public BufferedImage crop(BufferedImage src, Rectangle rect)
   {
       BufferedImage dest = new BufferedImage((int)rect.getWidth(), (int)rect.getHeight(), BufferedImage.TYPE_INT_ARGB);
       Graphics g = dest.getGraphics();
       g.drawImage(src, 0, 0, (int)rect.getWidth(), (int)rect.getHeight(), (int)rect.getX(), (int)rect.getY(), (int)rect.getX() + (int)rect.getWidth(), (int)rect.getY() +(int) rect.getHeight(), null);
       g.dispose();
       return dest;
   }
   public void paintComponent (Graphics page)
   {
       
         if(menu == 0)
         {
             
             if(!loaded)
             {
                 page.drawImage(load_screen,0,0,null);
                 page.setColor(Color.white);
                 page.setFont(loadfont );
                 if(num_files != 0)
                 {
                     page.drawString("LOADING "+(100*num_photos/num_files)+"%", 60, 390);
                 }
                
             }
             else
             {
                 for(int i = 0; i < num_photos; i++)
                 {
                     BufferedImage view_picture = Modphotos.get(i);
                     page.drawImage(view_picture, x_offset*(i%4), y_offset*(i/4)+y_scroll+panel_height, null);
                 }
                  //paint top panel
                  page.drawImage(top_panel,0,0,null);
             }
             
             
         }
         else
         {
             PuckCreator.paintComponent(page);
             //paint top panel
             //page.drawImage(top_panel,0,0,null);
         }
             
             
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
   public BufferedImage ModPic(BufferedImage source, int xshf, int yshf, int xsize, int ysize )
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
   public List<String> readSmallTextFile(String aFileName) throws IOException {
        final Charset ENCODING = StandardCharsets.UTF_8;   
           
        Path path = Paths.get(aFileName);
        return Files.readAllLines(path, ENCODING);
  }
  
  public void writeSmallTextFile(List<String> aLines, String aFileName) throws IOException {
        final Charset ENCODING = StandardCharsets.UTF_8; 
      
        Path path = Paths.get(aFileName);
        Files.write(path, aLines, ENCODING);
  }
  public int Mouse_Click(MouseEvent event)
  {
          point = event.getPoint();
          // photo viewer
          if(menu == 0)
          {
                int x_point = (int)point.getX();
                int y_point = (int)point.getY();
                
                //call to return to puck settings
                if(BACK.Contains(x_point,y_point))
                {
                    //CANCEL
                      return 1;
                }
                
                int x_photo = (x_point/x_offset);
                
                //takes into account the scroll position and offset from top panel
                int sub_y_point = (y_point-y_scroll-panel_height);
                int y_photo;
                
                int photo;
                
                //is y point in panel
                boolean y_panel = y_point < panel_height;
                
                if(sub_y_point > 0 && !y_panel)
                {
                    y_photo = (sub_y_point/y_offset);
                    
                    photo = x_photo + y_photo*4;
                    if(photo >= num_photos)
                    {
                        photo = -1;
                    }
                }
                else
                {
                    photo = -1;
                }
                
                
                if(photo >= 0)
                {
                   
                   //System.out.println(photoNames.get(photo));
                   PuckCreator = new PuckCreator_v1_3(photos.get(photo),"new puck");
                   
                   menu = 1;
                }  
          }
          // puck creator 
          else
          {
              //use back button
              int x_point = (int)point.getX();
              int y_point = (int)point.getY();
              int puck_stage = PuckCreator.getStage();
              
              if(puck_stage == 0)
              {
                  if(BACK.Contains(x_point,y_point))
                  {
                      menu = 0;
                  }
                  else if(NEXT.Contains(x_point,y_point))
                  {
                      PuckCreator.Progress();
                  }
                  
              }
              if(puck_stage == 1)
              {
                  
                  if(BACK.Contains(x_point,y_point))
                  {
                      PuckCreator.Previous();
                  }
                  else if(NEXT.Contains(x_point,y_point))
                  {
                      PuckCreator.Progress();
                  }
                  else if(RESET.Contains(x_point,y_point))
                  {
                      PuckCreator.ClearPoints();
                  }
                  
              }
              if(puck_stage == 2)
              {
                  
                  if(BACK.Contains(x_point,y_point))
                  {
                      PuckCreator.Previous();
                  }
                  else if(ACCEPT.Contains(x_point,y_point))
                  {
                      PuckCreator.Progress();
                  }
                  
              }
              if(puck_stage == 3)
              {
                  if(BACK.Contains(x_point,y_point))
                  {
                      PuckCreator.setPuckName("new puck");
                      keyboard.resetName();
                      PuckCreator.Previous();
                  }
                  else
                  {
                      keyboard.CheckKey(x_point,y_point);
                      if(keyboard.CheckEnter() )
                      {
                          final String FILE_NAME = "textures/pucks/pucks.txt";
                          
                          try {
                              List<String> lines = readSmallTextFile(FILE_NAME);
                              String name = keyboard.getName();
                              
                              if( !lines.contains(name))
                              {
                                  lines.add(keyboard.getName());
                                  writeSmallTextFile(lines, FILE_NAME);
                              }
               
                          } catch (IOException e) {
                          }
                          
                          PuckCreator.SaveFiles();
                          //clean up
                          PuckCreator.CleanUp();
                          //New Puck Created
                          return 2;
                      }
                      PuckCreator.setPuckName(keyboard.getName());
                   }
                   
                  
              }  
              
          }
          //Nothing to change outside of panel
          return 0;
  }
  public void Mouse_Press(MouseEvent event)
  {
        point = event.getPoint();
        if(menu == 0)
        {
            start_y = (int)point.getY();
        }
        else
        {
           PuckCreator.MousePoint((int)point.getX(),(int)point.getY());
        }
  }
  public void Mouse_Drag(MouseEvent event)
  {
          point = event.getPoint();
          if(menu == 0)
          {
         
                end_y = (int)point.getY();
                
                int diff_y = end_y - start_y;
                
                int new_y_scroll = y_scroll + diff_y;
                if( new_y_scroll > yscroll_min && new_y_scroll < yscroll_max)
                {
                    y_scroll =  new_y_scroll;
                }
                
                
                start_y = end_y;
          }
          else
          {
              
              PuckCreator.MouseControl((int)point.getX(),(int)point.getY());
              
          }
  }
  public void Zoom(MouseWheelEvent event)
  {
           int steps = event.getWheelRotation();
           
           if(menu == 1)
           {
               PuckCreator.Zoom(steps);
           }
  }
  //Background task for loading images.
  SwingWorker worker = new SwingWorker<Void, Void>() {
        @Override
        
       public Void doInBackground()
       {
           
         loadImages();
         return null;
       }
       protected void done() 
       { 
         loaded = true;
         
         int mod_ex = 1;
         if(num_photos%4 == 0)
         {
             mod_ex = 0;
         }
         
         yscroll_min = HEIGHT - (y_offset*(num_photos/4+mod_ex)+y_scroll+panel_height) ;
         if( yscroll_min > 0)
         {
             yscroll_min = 0;
         }
       }
 
    };
  
}
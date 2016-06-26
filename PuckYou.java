import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.ArrayList;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;
import java.text.*;

public class PuckYou extends JPanel
{
    //Table dimensions
    private final int WIDTH = 320, HEIGHT = 568;
    int border_x = 7; // pixels
    int border_y = 7; // pixels
    int table_x = WIDTH-border_x; 
    int table_y = HEIGHT-border_y;
    
    int xmin = (WIDTH-table_x)/2;
    int xmax = WIDTH-xmin;
    
    int ymin = (HEIGHT-table_y)/2;
    int ymax = HEIGHT-ymin;
    
    private final int DELAY = 10; //milliseconds
    private javax.swing.Timer timer;
    
    private PuckOff puckoff; // contains puckoff game info
    private Menus menus; // contains menu info
    private Photo_Viewer puck_create;
    
    int menu = 0; //keep track of what menu you are at
    
    //photos stored for photo viewer once loaded
    ArrayList<BufferedImage> cached_photos;
    ArrayList<BufferedImage> cached_modphotos;
    
    private Point mouse_point = new Point(0,0);
     public static void main (String[] args)
    {
        JFrame frame = new JFrame ("Puck You");
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new PuckYou() );
        
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        //frame.setResizable(false);
        
    }
    public PuckYou()
    {
  
        addMouseMotionListener (new DragListener());
        addMouseListener ( new ClickListener());
        addMouseWheelListener ( new Scroll());
        
        cached_photos = null;
        cached_modphotos = null;
        
        menus = new Menus();
        puckoff = new PuckOff(menus.getFace());
        
        timer = new javax.swing.Timer(DELAY, new TimerListener() );
    
        setPreferredSize (new Dimension(WIDTH,HEIGHT));
        setBackground (Color.black);
        timer.start();
        
        
    }
    public void paintComponent (Graphics page)
    {
        super.paintComponent(page);
        
        if(menu == 0)
        {
            menu = menus.DisplayIntro(page);
        }
        else if(menu == 1)
        {
            puckoff.paint_game(page);
            puckoff.Move();
        }
        else if(menu == 2)
        {
            puckoff.paint_game(page);
            menus.DisplayPause(page);
        }
        else if(menu == 3)
        {
            menus.DisplayPuckSettings(page);
        }
        else if(menu == 4)
        {
            puck_create.paintComponent(page);
        }
        else if(menu == 5)
        {
            menus.DisplayPuckDelete(page);
        }
        else if(menu == 6)
        {
            menus.DisplayOptions(page);
        }
        else if(menu == 7)
        {
            menus.DisplayMode(page);
        }
        else if(menu == 8)
        {
            menus.DisplayMain(page);
        }
    }
    private class TimerListener implements ActionListener
    { 
         public void actionPerformed ( ActionEvent event)
         {
             repaint();
         }
    }
    public class DragListener implements  MouseMotionListener
    {
        public void mouseMoved (MouseEvent event){}
        public void mouseClicked (MouseEvent event) {}
        public void mouseReleased (MouseEvent event) {}
        public void mouseEntered (MouseEvent event) {}            
        public void mouseExited (MouseEvent event) {}
        public void mouseDragged (MouseEvent event) 
        {
            if(menu == 1)
            {
                mouse_point = event.getPoint();
                puckoff.player_control.Drag_Mover(mouse_point);
            }
            else if(menu == 3)
            {
                mouse_point = event.getPoint();
                int x_point = (int)mouse_point.getX();
                int y_point = (int)mouse_point.getY();
                
                menus.Cycle(x_point,y_point);
                
            }
            else if(menu == 4)
            {
                puck_create.Mouse_Drag(event);
            }
            else if(menu == 6)
            {
                mouse_point = event.getPoint();
                int x_point = (int)mouse_point.getX();
                int y_point = (int)mouse_point.getY();
                
                menus.OptionsControl(x_point,y_point);
                
            }
                
        }
        public void mousePressed (MouseEvent event) {}                             
    }
    public class ClickListener implements MouseListener
    {
        public void mouseClicked (MouseEvent event)
        {
            if(menu == 2)
            {
                Point click_point = event.getPoint();
                int x_point = (int)click_point.getX();
                int y_point = (int)click_point.getY();
                
                int click_count = event.getClickCount();
                
                menu = menus.Pause_Resume(x_point,y_point,click_count);
                if( menus.Pause_Restart(x_point,y_point,click_count))
                {
                    puckoff.Reset();
                    menu = 1;
                }
                else if( menus.Puck_Settings(x_point,y_point,click_count))
                {
                    menus.set_selected_index();
                    menu = 3;
                }
                else if( menus.Game_Settings(x_point,y_point,click_count))
                {
                    menus.setValues();
                    menu = 6;
                }
                else if( menus.Pause_Quit(x_point,y_point,click_count))
                {
                    menu = 7;
                }
                
            }
            else if(menu == 3)
            {
                Point click_point = event.getPoint();
                int x_point = (int)click_point.getX();
                int y_point = (int)click_point.getY();
                
                if (menus.Puck_Accept(x_point, y_point))
                {
                    puckoff.setFace(menus.getFace());
                    menu = 2;
                }
                if (menus.Puck_Cancel(x_point, y_point))
                {
                    menu = 2;
                }
                if (menus.Puck_Create(x_point, y_point))
                {
                    puck_create = new Photo_Viewer(cached_photos,cached_modphotos);
                    menu = 4;
                }
                if (menus.Puck_Delete(x_point, y_point))
                {
                    menu = 5;
                }
            }
            else if(menu == 4)
            {
                int action = puck_create.Mouse_Click(event);
                
                if (action != 0 )
                {
                    //cancel and return to pucksettings
                    cached_photos = puck_create.getPhotos();
                    cached_modphotos = puck_create.getModphotos();
                    if(action == 1)
                    {
                        menu = 3;
                        puck_create.ClearPuck();
                        puck_create = null; 
                    }
                    //new puck puck created and return to pucksettings
                    else if(action == 2)
                    {
                        menus.ReadPucks();
                        menus.set_last_index();
                        menu = 3;
                        puck_create.ClearPuck();
                        puck_create = null; 
                    }
                    
                }
            }
            else if(menu == 5)
            {
                Point click_point = event.getPoint();
                int x_point = (int)click_point.getX();
                int y_point = (int)click_point.getY();
                
                if (menus.Puck_Delete_Yes(x_point, y_point))
                {
                    menus.DeletePuck();
                    menus.ReadPucks();
                    menus.set_last_index();
                    menu = 3;
                }
                if (menus.Puck_Delete_No(x_point, y_point))
                {
                    menu = 3;
                }
            }
            else if(menu == 6)
            {
                Point click_point = event.getPoint();
                int x_point = (int)click_point.getX();
                int y_point = (int)click_point.getY();
                
               if (menus.Option_Cancel(x_point, y_point))
               {
                    menu = 2;
               } 
               if (menus.Option_Accept(x_point, y_point))
               {
                   menus.AcceptValues();
                   
                   PuckOff.getPuck().setFric(menus.getValue_Bar1());
                   PuckOff.getPuck().setMxsp(menus.getValue_Bar2());
                   PuckOff.getPuck().setMnsp(menus.getValue_Bar3());
                   PuckOff.getPuck().setSS(menus.getValue_Bar4());
                   PuckOff.getPuck().setRadius((int)menus.getValue_Bar5());
                   PuckOff.getPuck().setRatio(menus.getValue_Bar6());
                   
                   menu = 2;
               } 
            }
            else if(menu == 7)
            {
               Point click_point = event.getPoint();
               int x_point = (int)click_point.getX();
               int y_point = (int)click_point.getY();
                
               if(menus.Puckoff_Difficulty(x_point, y_point))
               {
                   puckoff.setDifficulty(menus.getPuckoff_Diff());
               }
               if (menus.Puckoff_Start(x_point, y_point))
               {
                    menu = 1;
               } 
            }
            else if(menu == 8)
            {
               Point click_point = event.getPoint();
               int x_point = (int)click_point.getX();
               int y_point = (int)click_point.getY();
                
               if (menus.Start_Puckin(x_point, y_point))
               {
                    menu = 7;
               } 
            }
                
        }                    
        public void mousePressed (MouseEvent event) 
        {
            if(menu == 1)
            {
                mouse_point = event.getPoint();
                puckoff.player_control.Grab_Mover_Press(mouse_point);
                
                //check if paused
               if( menus.Paused(event) )
               {
                   menu = 2;
                   puckoff.player_control.Grab_Mover_Release();
               }
            }
            else if(menu == 4)
            {
                puck_create.Mouse_Press(event);
            }
            
        }
        public void mouseReleased (MouseEvent event) 
        {
            if(menu == 1)
            {
                mouse_point = event.getPoint();
                puckoff.player_control.Grab_Mover_Release();
            }
            else if(menu == 3)
            {
                menus.Reset_Puck_Pos();
            }
        }
        public void mouseEntered (MouseEvent event) {}
        public void mouseExited (MouseEvent event) {}
        public void mouseDragged (MouseEvent event) {}
        public void mouseMoved (MouseEvent event) {}
    }  
    private class Scroll implements MouseWheelListener
    {
       public void mouseWheelMoved(MouseWheelEvent event)
       {
           if(menu == 4)
           {
                puck_create.Zoom(event);
           }
          
       }
    }

    
}

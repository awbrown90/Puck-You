import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.ArrayList;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;
import java.text.*;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class Menus
{
    private final int WIDTH = 320, HEIGHT = 568;
    BufferedImage title, pause, pause_img, pause_mask, puck_settings, puck_delete, options, options_img, options_alpha, demo_screen, main_menu,
    puckoff_easy,puckoff_med, puckoff_hard, puckoff_crzy,
    bar1, bar2, bar3, bar4, bar5, bar6,
    puck_img , puck_alpha, puck;
    
    
    private MovBall2 displayPuck, demo_ball, demo_striker;
    private Circle demo_puck;
   
    private ArrayList<String> faces;
    private int face_index = 0;
    private int selected_index = face_index;
    
    double puck_x = 160;
    double puck_y = 350;
    
    private final int DELAY = 10; //milliseconds
    
    int title_time = 2000; // milliseconds
    int dis_title_time = title_time/DELAY; //converted display time for the title screen
    
    int dis_timer = 0;
    int demo_timer = 0;
    
    //MODE PUCKOFF DIFFICULTY 
    int puckoff_diff = 0;
    
    // //PAUSE BUTTONS// //
    
    //RESUME
    int resume_width = 200; //pixels
    int resume_height= 50;//pixels
   
    int resume_xoffset = 61;//pixels
    int resume_yoffset = 166;//pixels
    
    DisplayButton RESUME = new DisplayButton(resume_width, resume_height, resume_xoffset, resume_yoffset);
    
    //RESTART
    int restart_width = 200; //pixels
    int restart_height= 50;//pixels
   
    int restart_xoffset = 61;//pixels
    int restart_yoffset = 226;//pixels
    
    DisplayButton RESTART = new DisplayButton(restart_width, restart_height, restart_xoffset, restart_yoffset);
    
    //PUCK SETTINGS
    int pucks_width = 200; //pixels
    int pucks_height= 50;//pixels
   
    int pucks_xoffset = 61;//pixels
    int pucks_yoffset = 290;//pixels
    
    DisplayButton PUCK_SETTINGS = new DisplayButton(pucks_width, pucks_height, pucks_xoffset, pucks_yoffset);
    
    //GAME SETTINGS
    int options_width = 200; //pixels
    int options_height= 50;//pixels
   
    int options_xoffset = 61;//pixels
    int options_yoffset = 351;//pixels
    
    DisplayButton GAME_SETTINGS = new DisplayButton(options_width, options_height, options_xoffset, options_yoffset);
    
    //QUIT to main menu
    int quit_width = 200; //pixels
    int quit_height= 50;//pixels
   
    int quit_xoffset = 61;//pixels
    int quit_yoffset = 418;//pixels
    
    DisplayButton QUIT = new DisplayButton(quit_width, quit_height, quit_xoffset, quit_yoffset);
    
    // //MAIN MENU BUTTONS // //
    
    //START PUCKING
     int stpuckin_width = 200; //pixels
     int stpuckin_height= 50;//pixels
   
     int stpuckin_xoffset = 65;//pixels
     int stpuckin_yoffset = 199;//pixels
    
    DisplayButton START_PUCKIN = new DisplayButton(stpuckin_width, stpuckin_height, stpuckin_xoffset, stpuckin_yoffset);
    
    // //MODE SELECT BUTTONS // //
    
    //PUCKOFF START
     int postart_width = 103; //pixels
     int postart_height= 32;//pixels
   
     int postart_xoffset = 165;//pixels
     int postart_yoffset = 515;//pixels
    
    DisplayButton PUCKOFF_START = new DisplayButton(postart_width, postart_height, postart_xoffset, postart_yoffset);
    
    //PUCKOFF EASY
    int poeasy_width = 155; //pixels
    int poeasy_height= 30;//pixels
   
    int poeasy_xoffset = 26;//pixels
    int poeasy_yoffset = 416;//pixels
    
    DisplayButton PUCKOFF_EASY = new DisplayButton(poeasy_width, poeasy_height, poeasy_xoffset, poeasy_yoffset);
    
    //PUCKOFF MEDIUM
    int pomed_width = 155; //pixels
    int pomed_height= 30;//pixels
   
    int pomed_xoffset = 26;//pixels
    int pomed_yoffset = 452;//pixels
    
    DisplayButton PUCKOFF_MED = new DisplayButton(pomed_width, pomed_height, pomed_xoffset, pomed_yoffset);
    
    //PUCKOFF HARD
    int pohard_width = 155; //pixels
    int pohard_height= 30;//pixels
   
    int pohard_xoffset = 26;//pixels
    int pohard_yoffset = 486;//pixels
    
    DisplayButton PUCKOFF_HARD = new DisplayButton(pohard_width, pohard_height, pohard_xoffset, pohard_yoffset);
    
    //PUCKOFF CRAZY
    int pocrzy_width = 155; //pixels
    int pocrzy_height= 30;//pixels
   
    int pocrzy_xoffset = 26;//pixels
    int pocrzy_yoffset = 523;//pixels
    
    DisplayButton PUCKOFF_CRAZY = new DisplayButton(pocrzy_width, pocrzy_height, pocrzy_xoffset, pocrzy_yoffset);
    
    // //GAME SETTING BUTTONS // //
    
    //CANCEL
     int cancelop_width = 140; //pixels
     int cancelop_height= 37;//pixels
   
     int cancelop_xoffset = 8;//pixels
     int cancelop_yoffset = 523;//pixels
    
    DisplayButton OPTIONS_CANCEL = new DisplayButton(cancelop_width, cancelop_height, cancelop_xoffset, cancelop_yoffset);
    
    //ACCEPT
     int acceptop_width = 140; //pixels
     int acceptop_height= 37;//pixels
   
     int acceptop_xoffset = 203;//pixels
     int acceptop_yoffset = 515;//pixels
    
    DisplayButton OPTIONS_ACCEPT = new DisplayButton(acceptop_width, acceptop_height, acceptop_xoffset, acceptop_yoffset);
    
    // //PUCK SETTING BUTTONS // //
    
    //CANCEL
     int cancel_width = 140; //pixels
     int cancel_height= 50;//pixels
   
     int cancel_xoffset = 11;//pixels
     int cancel_yoffset = 510;//pixels
    
    DisplayButton PUCK_CANCEL = new DisplayButton(cancel_width, cancel_height, cancel_xoffset, cancel_yoffset);
    
    //ACCEPT
     int accept_width = 140; //pixels
     int accept_height= 50;//pixels
   
     int accept_xoffset = 165;//pixels
     int accept_yoffset = 510;//pixels
    
    DisplayButton PUCK_ACCEPT = new DisplayButton(accept_width, accept_height, accept_xoffset, accept_yoffset);
    
    //CREATE
     int create_width = 80; //pixels
     int create_height= 40;//pixels
   
     int create_xoffset = 230;//pixels
     int create_yoffset = 453;//pixels
    
    DisplayButton PUCK_CREATE = new DisplayButton(create_width, create_height, create_xoffset, create_yoffset);
    
    //DELTE
     int delete_width = 80; //pixels
     int delete_height= 40;//pixels
   
     int delete_xoffset = 8;//pixels
     int delete_yoffset = 453;//pixels
    
    DisplayButton PUCK_DELETE = new DisplayButton(delete_width, delete_height, delete_xoffset, delete_yoffset);
    
    //DELTE_YES
     int delete_yes_width = 100; //pixels
     int delete_yes_height= 40;//pixels
   
     int delete_yes_xoffset = 176;//pixels
     int delete_yes_yoffset = 423;//pixels
    
    DisplayButton PUCK_DELETE_YES = new DisplayButton(delete_yes_width, delete_yes_height, delete_yes_xoffset, delete_yes_yoffset);
    
    //DELTE_NO
     int delete_no_width = 100; //pixels
     int delete_no_height= 40;//pixels
   
     int delete_no_xoffset = 37;//pixels
     int delete_no_yoffset = 423;//pixels
    
    DisplayButton PUCK_DELETE_NO = new DisplayButton(delete_no_width, delete_no_height, delete_no_xoffset, delete_no_yoffset);
    
    
    // GAME SETTING 
    
    Font frictionfont_big = new Font("BankGothic Md BT", Font.PLAIN, 32);
    Font frictionfont_small = new Font("BankGothic Md BT", Font.PLAIN, 12);
    
    private int demo_active;
    
    int demo_wall_min = 8;
    int demo_wall_max = 8;
    int demo_ball_r;
    
    
    // GAME SETTING BARS
    
    int x_cushion = 34;
    int y_cushion = 8;
    
    //Scroll Bar 1 Friction
    double bar1_value;
    double bar1_set;
    double bar1_standard;
    int max_bar1 = 3;
    int min_bar1 = 0;
    
    private int bar1_x;
    private int bar1_width = 32; //pixels
    private int bar1_height = 19; //pixels
    
    private int bar1_yoffset = 171;//pixels
    
    //Scroll Bar 2 Max Speed
    double bar2_value;
    double bar2_set;
    double bar2_standard;
    int max_bar2 = 5000;
    int min_bar2 = 500;
    
    private int bar2_x;
    private int bar2_width = 32; //pixels
    private int bar2_height = 19; //pixels
    
    private int bar2_yoffset = 208;//pixels
    
    //Scroll Bar 3 Min Speed
    double bar3_value;
    double bar3_set;
    double bar3_standard;
    int max_bar3 = 200;
    int min_bar3 = 0;
    
    private int bar3_x;
    private int bar3_width = 32; //pixels
    private int bar3_height = 19; //pixels
    
    private int bar3_yoffset = 247;//pixels
    
    //Scroll Bar 4 Strike Strength
    double bar4_value;
    double bar4_set;
    double bar4_standard;
    double max_bar4 =  4;
    double min_bar4 = .5;
    
    private int bar4_x;
    private int bar4_width = 32; //pixels
    private int bar4_height = 19; //pixels
    
    private int bar4_yoffset = 283;//pixels
    
    //Scroll Bar 5 Puck Size
    double bar5_value;
    double bar5_set;
    double bar5_standard;
    int max_bar5 = 50;
    int min_bar5 = 5;
    
    private int bar5_x;
    private int bar5_width = 32; //pixels
    private int bar5_height = 19; //pixels
    
    private int bar5_yoffset = 321;//pixels
    
     //Scroll Bar 6 Image Size
    double bar6_value;
    double bar6_set;
    double bar6_standard;
    int max_bar6 = 3;
    int min_bar6 = 1;
    
    private int bar6_x;
    private int bar6_width = 32; //pixels
    private int bar6_height = 19; //pixels
    
    private int bar6_yoffset = 353;//pixels
    
    int cycle_min = 100;
    int cycle_max = 220;
    
    Font introfont = new Font("TimesRoman", Font.PLAIN, 20);
    Font puckfont_big = new Font("BankGothic Md BT", Font.PLAIN, 25);
    Font puckfont_small = new Font("BankGothic Md BT", Font.PLAIN, 18);
    
    // //menu system// //
    
    //intro: 0
    //game: 1
    //pause: 2
    //puck setting: 3
    //puck creator: 4
    //puck delete: 5
    //game settings: 6
    //mode select: 7
    //main menu: 8
    
    public Menus()
    {
       
       LoadImages();
       
       //Define Bar starting positions
       bar1_standard = .4;
       bar2_standard = 1200;
       bar3_standard = 30;
       bar4_standard = 1;
       bar5_standard = 26;
       bar6_standard = 1.5;
       
       bar1_set = bar1_standard;
       bar2_set = bar2_standard;
       bar3_set = bar3_standard;
       bar4_set = bar4_standard;
       bar5_set = bar5_standard;
       bar6_set = bar6_standard;
       
       faces = new ArrayList<String>();
       ReadPucks();
       
       Circle circle_puck = new Circle(26,Color.black,puck_x, puck_y, 1.8, getFace() );
       displayPuck = new MovBall2(circle_puck, 30, 90, false);
       
       demo_ball_r = 35;
       
       Circle ball = new Circle(demo_ball_r,Color.black,demo_wall_min+demo_ball_r, HEIGHT/2+175,1, "puck face");
       demo_ball = new MovBall2(ball, 500, 0, true);
       
       demo_puck = new Circle(demo_ball_r,Color.black,WIDTH/2, HEIGHT/2+175,1, "puck");
       
       Circle striker = new Circle(demo_ball_r,Color.black,demo_wall_min-demo_ball_r, HEIGHT/2+175,1, "blue");
       demo_striker = new MovBall2(striker, 300, 0, true);
       
       demo_ball.AddCollision(demo_striker);
       
       demo_active = 0;
       
    }
    public void LoadImages()
    {
        
        try{
           title = ImageIO.read(new File("textures/bad_apps.jpg"));
           
           puckoff_easy = ImageIO.read(new File("textures/mode_po_easy.jpg"));
           puckoff_med = ImageIO.read(new File("textures/mode_po_med.jpg"));
           puckoff_hard = ImageIO.read(new File("textures/mode_po_hard.jpg"));
           puckoff_crzy = ImageIO.read(new File("textures/mode_po_crzy.jpg"));
           
           main_menu = ImageIO.read(new File("textures/main_menu.jpg"));
           pause_img = ImageIO.read(new File("textures/pause.jpg"));
           pause_mask = ImageIO.read(new File("textures/pause_alpha.jpg"));
           puck_settings = ImageIO.read(new File("textures/puck_settings.jpg"));
           puck_delete = ImageIO.read(new File("textures/puck_delete.jpg"));
           options_img = ImageIO.read(new File("textures/game_settings.jpg"));
           options_alpha = ImageIO.read(new File("textures/game_settings_alpha.jpg"));
           demo_screen = ImageIO.read(new File("textures/game_settings_demo.jpg"));
           
           
           bar1 = ImageIO.read(new File("textures/scroll.jpg"));
           bar2 = bar1;
           bar3 = bar1;
           bar4 = bar1;
           bar5 = bar1;
           bar6 = bar1;
          } catch (IOException e) {
          }
          pause = GraphicsLab.AlphaMask(pause_img,pause_mask);
          options = GraphicsLab.AlphaMask(options_img,options_alpha);
        
    }
    public void ReadPucks()
    {
        try {
        ReadTextFile("textures/pucks/pucks.txt");
        } catch (IOException e) {
        }
        
    }
    public void ReadTextFile(String aFileName) throws IOException 
    {
        faces.clear();
        final Charset ENCODING = StandardCharsets.UTF_8;
        
        Path path = Paths.get(aFileName);
        try (Scanner scanner =  new Scanner(path, ENCODING.name())){
          while (scanner.hasNextLine()){
            //process each line in some way
            faces.add(scanner.nextLine());
            //log(scanner.nextLine());
          }      
        }
        
   }
   public void DeletePuck()
   {
       
      final String FILE_NAME = "textures/pucks/pucks.txt";
      
      EraseImage(faces.get(face_index));
                          
      try {
          List<String> lines = readSmallTextFile(FILE_NAME);
          lines.remove(face_index);
          writeSmallTextFile(lines, FILE_NAME);
      } catch (IOException e) {
      }
      
   }
   public static void EraseImage(String filename)
   {
       //typer: true for image, false for mask
       Path path_img;
       Path path_alpha;
       
        String file_img = filename;
        String file_alpha = filename+"_alpha";
       
        path_img = Paths.get("textures/pucks/"+file_img+".png");
        path_alpha = Paths.get("textures/pucks/"+file_alpha+".png");
        
        try {
            Files.delete(path_img);
            Files.delete(path_alpha);
        } catch (IOException e) {
        }
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
    public String getFace()
    {
        return faces.get(face_index);
    }
    public int getIndex()
    {
        return face_index;
    }
    public int getPuckoff_Diff()
    {
        return puckoff_diff;
    }
    public void set_selected_index()
    {
        face_index = selected_index;
        displayPuck.getCircle().Face(faces.get(face_index)); 
    }
    public void set_last_index()
    {
        face_index = faces.size()-1;
        displayPuck.getCircle().Face(faces.get(face_index));     
    }
    private void DrawCenteredString(String s, int XPos, int YPos, Graphics g)
    {  
        
        int width = 320;
        
        int stringLen = (int)
        g.getFontMetrics().getStringBounds(s, g).getWidth();  
        int start = width/2 - stringLen/2;  
        
        g.drawString(s, start + XPos, YPos);  
    } 
    // Read and displayed during Start
    public int DisplayIntro(Graphics g)
    { 
          g.setFont(introfont );
          g.setColor(Color.white);
          
          if(dis_timer >=  dis_title_time)
          { 
              dis_timer = 0;
              return 8; // pass to puckoff
          }
          else
          {
              g.drawImage(title,0,0,null);
              g.drawString("Alpha V0.10", 180, 25);
              //page.drawImage(title,0,0,null);
              dis_timer++;
              return 0; // pass to self
          }
    }
    public void DisplayMode(Graphics g)
    {
        if(puckoff_diff == 0)
        {
            g.drawImage(puckoff_easy,0,0,null);
        }
        else if(puckoff_diff == 1)
        {
            g.drawImage(puckoff_med,0,0,null);
        }
        else if(puckoff_diff == 2)
        {
            g.drawImage(puckoff_hard,0,0,null);
        }
        else if(puckoff_diff == 3)
        {
            g.drawImage(puckoff_crzy,0,0,null);
        }
    }
    public void DisplayOptions(Graphics g)
    {
        
        g.drawImage(demo_screen,0,0,null);
        
        if(demo_active == 1)
        {
            g.setColor(Color.white);
            g.setFont(frictionfont_big);
            DrawCenteredString("Friction",0,434,g); 
            g.setFont(frictionfont_small);
            g.drawString("Initial Speed", 20, 494);
            g.drawString("500 px/sec", 20, 505);
            g.drawString("Final Speed", 220, 494);
            g.drawString(Round(demo_ball.getSpeed(),0)+" px/sec", 215, 505);
            
            demo_ball.draw(g);
            demo_slide(demo_timer,1);
            
        }
        else if(demo_active == 2)
        {
            g.setColor(Color.white);
            g.setFont(frictionfont_big);
            DrawCenteredString("Max Speed",0,434,g);
            
            demo_ball.draw(g);
            demo_slide(demo_timer,2);
            
        }
        else if(demo_active == 3)
        {
            g.setColor(Color.white);
            g.setFont(frictionfont_big);
            DrawCenteredString("Min Speed",0,434,g);
            
            demo_ball.draw(g);
            demo_slide(demo_timer,3);
            
        }
        else if(demo_active ==4)
        {
            g.setColor(Color.white);
            g.setFont(frictionfont_big);
            DrawCenteredString("Strike Ratio",0,434,g);
            g.setFont(frictionfont_small);
            g.drawString("Stiker Speed", 20, 494);
            g.drawString("300 px/sec", 20, 505);
            g.drawString("Puck Speed", 220, 494);
            g.drawString(Round(demo_ball.getSpeed(),0)+" px/sec", 215, 505);
            
            demo_ball.draw(g);
            demo_striker.draw(g);
            demo_strike();
        }
        else if(demo_active == 5)
        {
            g.setColor(Color.white);
            g.setFont(frictionfont_big);
            DrawCenteredString("Puck Size",0,434,g);
            
            demo_ball.draw(g);
            demo_puck.draw(g);
            
        }
        else if(demo_active == 6)
        {
            g.setColor(Color.white);
            g.setFont(frictionfont_big);
            DrawCenteredString("Image Size",0,434,g);
            
            demo_ball.draw(g);
            demo_puck.draw(g);
            
        }
        
        g.drawImage(options,0,0,null);
        g.drawImage(bar1,bar1_x,bar1_yoffset,null);
        
        g.setColor(Color.white);
        g.setFont(frictionfont_small);
        
        g.drawString(Double.toString(Round(bar1_value,2)),260,bar1_yoffset);
        g.drawImage(bar2,bar2_x,bar2_yoffset,null);
        g.drawString(Double.toString(Round(bar2_value,0)),260,bar2_yoffset);
        g.drawImage(bar3,bar3_x,bar3_yoffset,null);
        g.drawString(Double.toString(Round(bar3_value,0)),260,bar3_yoffset);
        g.drawImage(bar4,bar4_x,bar4_yoffset,null);
        g.drawString(Double.toString(Round(bar4_value,2)),260,bar4_yoffset);
        g.drawImage(bar5,bar5_x,bar5_yoffset,null);
        g.drawString(Double.toString(Round(bar5_value,0)),260,bar5_yoffset);
        g.drawImage(bar6,bar6_x,bar6_yoffset,null);
        g.drawString(Double.toString(Round(bar6_value,1)),260,bar6_yoffset);
        
            
    }
    public void demo_settings(int state)
    {
        if( state == 1)
        {
            demo_ball.setFric(bar1_value);
            demo_ball.setMxsp(500);
            demo_ball.setMnsp(0);
            //demo_ball.setSS();
            demo_ball.setRadius(35);
            demo_ball.setRatio(1);
            
            //set aside striker
            demo_striker.getCircle().setX(-2*demo_ball_r);
        }
        if( state == 2)
        {
            demo_ball.setFric(0);
            //demo_ball.setMxsp(0);
            //demo_ball.setMnsp();
            //demo_ball.setSS();
            demo_ball.setRadius(35);
            demo_ball.setRatio(1);
            
            //setting the speed for display puck
            demo_ball.setSpeed(bar2_value);
            
            //set aside striker
            demo_striker.getCircle().setX(-2*demo_ball_r);
        }
        if( state == 3)
        {
            demo_ball.setFric(0);
            //demo_ball.setMxsp();
            //demo_ball.setMnsp();
            //demo_ball.setSS();
            demo_ball.setRadius(35);
            demo_ball.setRatio(1);
            
            //setting the speed for display puck
            demo_ball.setSpeed(bar3_value);
            
            //set aside striker
            demo_striker.getCircle().setX(-2*demo_ball_r);
        }
        if( state == 4)
        {
            
            demo_ball.setFric(0);
            demo_ball.setMxsp(300*4);
            //demo_ball.setMnsp();
            demo_ball.setSS(bar4_value);
            demo_ball.setRadius(35);
            demo_ball.setRatio(1);
        }
        if( state == 5)
        {
            // Center Position
            demo_ball.getCircle().setX(WIDTH/2);
            
            //demo_ball.setFric();
            //demo_ball.setMxsp();
            //demo_ball.setMnsp();
            //demo_ball.setSS();
            demo_ball.setRadius((int)bar5_value);
            demo_ball.setRatio(bar6_value);
            
            demo_puck.setRadius((int)bar5_value);
            demo_puck.setRatio(1);
            
            //set aside striker
            demo_striker.getCircle().setX(-2*demo_ball_r);
        }
        if( state == 6)
        {
            // Center Position
            demo_ball.getCircle().setX(WIDTH/2);
            
            //demo_ball.setFric();
            //demo_ball.setMxsp();
            //demo_ball.setMnsp();
            //demo_ball.setSS();
            demo_ball.setRadius((int)bar5_value);
            demo_ball.setRatio(bar6_value);
            
            //set aside striker
            demo_striker.getCircle().setX(-2*demo_ball_r);
            
        }
        
        
    }
    public void OptionsControl(int x_point, int y_point)
    {
        if( (x_point >= bar1_x - x_cushion && x_point <= bar1_x+bar1_width+x_cushion) && (y_point >= bar1_yoffset - y_cushion && y_point <= bar1_yoffset+bar1_height+y_cushion) )
        {
            if(x_point >= (23+bar1_width/2) && x_point <= (WIDTH-23-bar1_width/2))
            {
                demo_active = 1;
                
                bar1_x = x_point-bar1_width/2;
                bar1_value = min_bar1+(max_bar1-min_bar1)*(double)(bar1_x-23)/242;
                
                demo_settings(demo_active);
                
            }
        }
        if( (x_point >= bar2_x - x_cushion && x_point <= bar2_x+bar2_width+x_cushion) && (y_point >= bar2_yoffset - y_cushion && y_point <= bar2_yoffset+bar2_height+y_cushion) )
        {
            if(x_point >= (23+bar2_width/2) && x_point <= (WIDTH-23-bar2_width/2))
            {
                demo_active = 2;
                
                bar2_x = x_point-bar2_width/2;
                bar2_value = min_bar2+(max_bar2-min_bar2)*(double)(bar2_x-23)/242;
                
                demo_settings(demo_active);
                
            }
        }
        if( (x_point >= bar3_x - x_cushion && x_point <= bar3_x+bar3_width+x_cushion) && (y_point >= bar3_yoffset - y_cushion && y_point <= bar3_yoffset+bar3_height+y_cushion) )
        {
            if(x_point >= (23+bar3_width/2) && x_point <= (WIDTH-23-bar3_width/2))
            {
                demo_active = 3;
                
                bar3_x = x_point-bar3_width/2;
                bar3_value = min_bar3+(max_bar3-min_bar3)*(double)(bar3_x-23)/242;
                
                demo_settings(demo_active);
                
            }
        }
        if( (x_point >= bar4_x - x_cushion && x_point <= bar4_x+bar4_width+x_cushion) && (y_point >= bar4_yoffset - y_cushion && y_point <= bar4_yoffset+bar4_height+y_cushion) )
        {
            if(x_point >= (23+bar4_width/2) && x_point <= (WIDTH-23-bar4_width/2))
            {
                demo_active = 4;
                
                bar4_x = x_point-bar4_width/2;
                bar4_value = min_bar4+(max_bar4-min_bar4)*(double)(bar4_x-23)/242;
                
                demo_settings(demo_active);
                
            }
        }
        if( (x_point >= bar5_x - x_cushion && x_point <= bar5_x+bar5_width+x_cushion) && (y_point >= bar5_yoffset - y_cushion && y_point <= bar5_yoffset+bar5_height+y_cushion) )
        {
            if(x_point >= (23+bar5_width/2) && x_point <= (WIDTH-23-bar5_width/2))
            {
                demo_active = 5;
                
                bar5_x = x_point-bar5_width/2;
                bar5_value = min_bar5+(max_bar5-min_bar5)*(double)(bar5_x-23)/242;
                
                demo_settings(demo_active);
          
            }
        }
        if( (x_point >= bar6_x - x_cushion && x_point <= bar6_x+bar6_width+x_cushion) && (y_point >= bar6_yoffset - y_cushion && y_point <= bar6_yoffset+bar6_height+y_cushion) )
        {
            if(x_point >= (23+bar6_width/2) && x_point <= (WIDTH-23-bar6_width/2))
            {
                demo_active = 6;
                
                bar6_x = x_point-bar6_width/2;
                bar6_value = min_bar6+(max_bar6-min_bar6)*(double)(bar6_x-23)/242;
                
                demo_settings(demo_active);
                
            }
        }
        
    }
    public double getValue_Bar1()
    {
        return bar1_value;
    }
    public double getValue_Bar2()
    {
        return bar2_value;
    }
    public double getValue_Bar3()
    {
        return bar3_value;
    }
    public double getValue_Bar4()
    {
        return bar4_value;
    }
    public double getValue_Bar5()
    {
        return bar5_value;
    }
    public double getValue_Bar6()
    {
        return bar6_value;
    }
    public void setValue_Bar1(double target)
    {
        bar1_x = (int)((target-min_bar1)*(242)/(max_bar1-min_bar1)+23);
        bar1_value = min_bar1+(max_bar1-min_bar1)*(double)(bar1_x-23)/242;
    }
    public void setValue_Bar2(double target)
    {
        bar2_x = (int)((target-min_bar2)*(242)/(max_bar2-min_bar2)+23);
        bar2_value = min_bar2+(max_bar2-min_bar2)*(double)(bar2_x-23)/242;
    }
    public void setValue_Bar3(double target)
    {
        bar3_x = (int)((target-min_bar3)*(242)/(max_bar3-min_bar3)+23);
        bar3_value = min_bar3+(max_bar3-min_bar3)*(double)(bar3_x-23)/242;
    }
    public void setValue_Bar4(double target)
    {
        bar4_x = (int)((target-min_bar4)*(242)/(max_bar4-min_bar4)+23);
        bar4_value = min_bar4+(max_bar4-min_bar4)*(double)(bar4_x-23)/242;
    }
    public void setValue_Bar5(double target)
    {
        bar5_x = (int)((target-min_bar5)*(242)/(max_bar5-min_bar5)+23);
        bar5_value = min_bar5+(max_bar5-min_bar5)*(double)(bar5_x-23)/242;
    }
    public void setValue_Bar6(double target)
    {
        bar6_x = (int)((target-min_bar6)*(242)/(max_bar6-min_bar6)+23);
        bar6_value = min_bar6+(max_bar6-min_bar6)*(double)(bar6_x-23)/242;
    }
    public void AcceptValues()
    {
        bar1_set = bar1_value;
        bar2_set = bar2_value;
        bar3_set = bar3_value;
        bar4_set = bar4_value;
        bar5_set = bar5_value;
        bar6_set = bar6_value;
    }
    public void setValues()
    {
        setValue_Bar1(bar1_set); //fric
        setValue_Bar2(bar2_set); //max sp
        setValue_Bar3(bar3_set); //min sp
        setValue_Bar4(bar4_set); //strike strength
        setValue_Bar5(bar5_set); //puck size
        setValue_Bar6(bar6_set); //photo size
    } 
    public void demo_slide(int timer_value, int demo_type)
    {
        if(timer_value < 300 || demo_type != 1) // if timer is less than 3 seconds using DELAY = 10
        {
            if(demo_ball.getCircle().getX()+demo_ball_r > (WIDTH-demo_wall_max) )// too far right
            {
                demo_ball.setAngle(180);
            }
            else if(demo_ball.getCircle().getX()-demo_ball_r < demo_wall_min) // too far left
            {
                demo_ball.setAngle(0);
            }
            demo_ball.Move();
            demo_timer++;
        }
        else 
        {
            demo_ball.getCircle().setX(demo_wall_min+demo_ball_r);
            demo_ball.setAngle(0);
            demo_ball.setSpeed(500);
            demo_timer = 0;
        }
            
    } 
    public void demo_strike()
    {
        //Set up if at beginning move
        if( demo_striker.getCircle().getX() <= -demo_ball_r)
        {
            demo_ball.getCircle().setX(WIDTH/2);
            demo_ball.setSpeed(0);
        }
        
        if( demo_ball.getCircle().getX() < WIDTH+demo_ball_r)
        {
            if( demo_striker.getCircle().getX() < WIDTH/2-(2*demo_ball_r-1))
            {
                demo_striker.Move();
            }
            demo_ball.Move();
        }
        else
        {
            demo_striker.getCircle().setX(-demo_ball_r);
        }
            
    }
    // round a to certain number of decimal places 0 - to whatever
    public double Round(double num, int places)
    {
        int divider = 1;
        while( places != 0)
        {
            num *= 10;
            divider *= 10;
            places--;
        }
        return (double)((int)num)/divider;
    }
    
    public void DisplayPause(Graphics g)
    {
        g.drawImage(pause,0,0,null);
    }
    public void DisplayMain(Graphics g)
    {
        g.drawImage(main_menu,0,0,null);
    }
    public void DisplayPuckSettings(Graphics g)
    {
        g.drawImage(puck_settings,0,0,null);
        displayPuck.draw(g);
        
        String s = getFace();
        int letters = s.length();
        if(letters < 8)
        {
            g.setFont(puckfont_big);
        }
        else
        {
            g.setFont(puckfont_small);
        }
        g.setColor(Color.black);
        DrawCenteredString(s,0,295,g);
    }
    public void DisplayPuckDelete(Graphics g)
    {
        g.drawImage(puck_delete,0,0,null);
        displayPuck.draw(g);
        
        g.setColor(Color.black);
        g.setFont(puckfont_small);
        DrawCenteredString("are you sure",0,275,g);
        DrawCenteredString("you want to delete",0,295,g);
        
        String s = getFace();
        int letters = s.length();
        if(letters < 8)
        {
            g.setFont(puckfont_big);
        }
        else
        {
            g.setFont(puckfont_small);
        }
        DrawCenteredString(getFace(),0,315,g);
        
    }
    // Read during Game (sense pause action)
    public boolean Paused(MouseEvent event)
    {
        if (event.getClickCount() == 2) 
        {
              return true; // pass to pause  menu = 2
        }
        else
        {
              return false; // pass to self menu = 1
        }
        
    }
    // Read during MAIN MENU
    public boolean Start_Puckin(int x_point, int y_point)
    {
        if(START_PUCKIN.Contains(x_point, y_point) )
        {
            return true; // pass to puckoff game 
        }
        else
        {
            return false; // pass to self 
        }
        
    }
    
    // Read during MODE SELECT 
    public boolean Puckoff_Start(int x_point, int y_point)
    {
        if(PUCKOFF_START.Contains(x_point, y_point) )
        {
            return true; // pass to puckoff game 
        }
        else
        {
            return false; // pass to self 
        }
        
    }
    // Read during MODE SELECT 
    public boolean Puckoff_Difficulty(int x_point, int y_point)
    {
        if(PUCKOFF_EASY.Contains(x_point, y_point) )
        {
            puckoff_diff = 0;
            return true;
        }
        else if(PUCKOFF_MED.Contains(x_point, y_point) )
        {
            puckoff_diff = 1; 
            return true;
        }
        else if(PUCKOFF_HARD.Contains(x_point, y_point) )
        {
            puckoff_diff = 2; 
            return true;
        }
        else if(PUCKOFF_CRAZY.Contains(x_point, y_point) )
        {
            puckoff_diff = 3; 
            return true;
        }
        return false;
    }
    //Read during Pause
    public boolean Game_Settings(int x_point, int y_point, int count)
    {
        if(GAME_SETTINGS.Contains(x_point, y_point) && count < 2)
        {
            return true; // pass to game 
        }
        else
        {
            return false; // pass to self 
        }
        
    }
    // Read during Pause
    public int Pause_Resume(int x_point, int y_point, int count)
    {
        if(RESUME.Contains(x_point, y_point) && count < 2)
        {
            return 1; // pass to game 
        }
        else
        {
            return 2; // pass to self 
        }
    }
    //boolean output for multiple triggers
    public boolean Pause_Restart(int x_point, int y_point, int count)
    {  
        if(RESTART.Contains(x_point, y_point) && count < 2)
        {
            return true; // pass to game menu = 1
        }
        else
        {
            return false; // pass to self menu = 2
        }
    }
    public boolean Pause_Quit(int x_point, int y_point, int count)
    {  
        if(QUIT.Contains(x_point, y_point) && count < 2)
        {
            return true; // pass to game menu = 1
        }
        else
        {
            return false; // pass to self menu = 2
        }
    }
    public boolean Option_Cancel(int x_point, int y_point)
    {  
        if(OPTIONS_CANCEL.Contains(x_point, y_point) )
        {
            demo_active = 0;
            demo_timer = 0;
            return true; // pass to pause = 2
        }
        else
        {
            return false; // pass to self menu = 6
        }
    }
    public boolean Option_Accept(int x_point, int y_point)
    {  
        if(OPTIONS_ACCEPT.Contains(x_point, y_point) )
        {
            demo_active = 0;
            demo_timer = 0;
            return true; // pass to pause = 2
        }
        else
        {
            return false; // pass to self menu = 6
        }
    }
    public boolean Puck_Settings(int x_point, int y_point, int count)
    {
        if(PUCK_SETTINGS.Contains(x_point, y_point) && count < 2)
        {
            return true; //pass to puck settings menu = 3
        }
        else
        {
            return false; // pass to self menu = 2
        }
    }
    public boolean Puck_Cancel(int x_point, int y_point)
    {
        if(PUCK_CANCEL.Contains(x_point, y_point) )
        {
            return true; //pass to pause menu = 2
        }
        else
        {
            return false; // pass to self menu =3
        }
    }
    public boolean Puck_Accept(int x_point, int y_point)
    {
        if(PUCK_ACCEPT.Contains(x_point, y_point) )
        {
            selected_index = face_index;
            return true; //pass to pause menu = 2
        }
        else
        {
            return false; // pass to self menu =3
        }
    }
    public boolean Puck_Create(int x_point, int y_point)
    {
        if(PUCK_CREATE.Contains(x_point, y_point) )
        {
            return true; //pass to pause menu = 2
        }
        else
        {
            return false; // pass to self menu =3
        }
    }
    public boolean Puck_Delete(int x_point, int y_point)
    {
        if(PUCK_DELETE.Contains(x_point, y_point) )
        {
            displayPuck.getCircle().setY(370);
            return true; //pass to pause menu = 2
            
        }
        else
        {
            return false; // pass to self menu =3
        }
    }
    public boolean Puck_Delete_Yes(int x_point, int y_point)
    {
        if(PUCK_DELETE_YES.Contains(x_point, y_point) )
        {
            Reset_Puck_Pos();
            return true; //pass to pause menu = 2
        }
        else
        {
            return false; // pass to self menu =3
        }
    }
    public boolean Puck_Delete_No(int x_point, int y_point)
    {
        if(PUCK_DELETE_NO.Contains(x_point, y_point) )
        {
            Reset_Puck_Pos();
            return true; //pass to pause menu = 2
        }
        else
        {
            return false; // pass to self menu =3
        }
    }
    public void Cycle(int x_point, int y_point)
    {
        
        boolean cycle_active = displayPuck.Contains((int)x_point, (int)y_point) ; 
        
        if(cycle_active)
        {
            if(x_point > cycle_min && x_point < cycle_max)
            {
                displayPuck.getCircle().setX(x_point);
            }
            else
            {
                if(x_point <= cycle_min)
                {
                   if(face_index <= 0)
                   {
                       face_index = faces.size()-1;
                   }
                   else
                   {
                       face_index--;
                   }
                }
                if(x_point >= cycle_max)
                {
                   if(face_index >= faces.size()-1)
                   {
                       face_index = 0;
                   }
                   else
                   {
                       face_index++;
                   }
                }
                displayPuck.getCircle().Face(faces.get(face_index));
                
                
                Reset_Puck_Pos();
            }
        }
        else
        {
            Reset_Puck_Pos();
        }
    }
    public void Reset_Puck_Pos()
    {
        displayPuck.getCircle().setX(puck_x);
        displayPuck.getCircle().setY(puck_y);
    }
    
        
}

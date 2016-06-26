import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.ArrayList;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;
import java.text.*;

public class BallController extends JPanel
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
    
    private static MovBall2 ball;
    private static int ball_r;
    private static Point point = new Point(0,0);
    private static Point click_point = null;
    
    //check when player grabs for the stick
    private static boolean grab = false;
    private static double mouse_delta_x = 0;
    private static double mouse_delta_y = 0;
    
    private final int DELAY = 10; //milliseconds
    private double sp_coef = 1000/DELAY ;
    
    public BallController(MovBall2 input_ball)
    {
        

        ball = input_ball;
        ball_r = ball.getCircle().getRadius();
       
        //set point location to starting position for player
        point.setLocation(WIDTH/2,HEIGHT/2+230);
    }
    public void move()
    {
        if(point != null)
        {
            double pre_player_x = ball.getCircle().getX();
            double pre_player_y = ball.getCircle().getY();
            
            if(grab)
            {
                 double grab_x = point.x-mouse_delta_x;
                 double grab_y = point.y-mouse_delta_y;
                 
                 boolean inArenaCenter = ((grab_x >=ball_r+xmin) && (grab_x <= (xmax-ball_r)) && (grab_y >=ball_r+(HEIGHT/2)) && (grab_y <= (ymax-ball_r)));
                 
                 if(inArenaCenter)
                 {
                     ball.getCircle().setX(grab_x);
                     ball.getCircle().setY(grab_y);
                 }
                 //Else try to fit stick to boundaries
                 else
                 {
                     
                     boolean xleft = grab_x < xmin+ball_r;
                     boolean xright = grab_x > xmax-ball_r;
                     boolean yup = grab_y < (HEIGHT/2)+ball_r;
                     boolean ydown = grab_y > ymax-ball_r;
                     
                     
                     if(xleft)
                     {
                         if(yup)
                         {
                               ball.getCircle().setX(xmin+ball_r);
                               ball.getCircle().setY((HEIGHT/2)+ball_r);
                         }
                         else if(ydown)
                         {
                               ball.getCircle().setX(xmin+ball_r);
                               ball.getCircle().setY(ymax-ball_r);
                         }
                         else
                         {
                               ball.getCircle().setX(xmin+ball_r);
                               ball.getCircle().setY(grab_y);
                         }
                     }
                     else if(xright)
                     {
                         if(yup)
                         {
                             ball.getCircle().setX(xmax-ball_r);
                             ball.getCircle().setY((HEIGHT/2)+ball_r);
                         }
                         else if(ydown)
                         {
                             ball.getCircle().setX(xmax-ball_r);
                             ball.getCircle().setY(ymax-ball_r);
                         }
                         else
                         {
                             ball.getCircle().setX(xmax-ball_r);
                             ball.getCircle().setY(grab_y);
                         }   
                     }
                     else if(yup)
                     {
                         ball.getCircle().setX(grab_x);
                         ball.getCircle().setY((HEIGHT/2)+ball_r);
                     }
                     else
                     {
                         ball.getCircle().setX(grab_x);
                         ball.getCircle().setY(ymax-ball_r);
                     }
                 }
                 
                 ball.OverlapAvoid();   
                 
            }
            
            double post_player_x = ball.getCircle().getX();
            double post_player_y = ball.getCircle().getY();
            
            //calculate player speed based on previous paddle postion and the new paddle position
            double mouse_dis_x =(post_player_x - pre_player_x);
            double mouse_dis_y =(post_player_y - pre_player_y);
        
            double mouse_speed = sp_coef * Math.sqrt(mouse_dis_x*mouse_dis_x+mouse_dis_y*mouse_dis_y);
            ball.setSpeed(mouse_speed);
            if(pre_player_x!=post_player_x||pre_player_y!=post_player_y)
            {
                ball.setAngle(180/Math.PI*ball.AngleTo(pre_player_x,pre_player_y,post_player_x,post_player_y));
            }
            else
            {
                ball.setAngle(-1);
            }
        
        
        }
    }
    public void ResetPoint()
    {
        grab = false;
        point.setLocation(WIDTH/2,HEIGHT/2+230);
    }
    public static void Drag_Mover(Point mouse_point)
    {
        
        if(grab){
            point = mouse_point;
            
        }
        
        
    }
    public static void Grab_Mover_Press(Point click_point)
    {
        //if player is touching stick then grab is good
       
        mouse_delta_x = click_point.x-ball.getCircle().getX();
        mouse_delta_y = click_point.y-ball.getCircle().getY();
                   
        boolean TouchingStick = Math.sqrt( Math.pow( (mouse_delta_x),2) + Math.pow( (mouse_delta_y), 2 ) ) <= ball_r;
          
        if(TouchingStick) {  
            grab = true;
            point = click_point;
        }
    }
    public static void Grab_Mover_Release()
    {
        //the player has let go of the stick
        grab = false;
    }
    
}

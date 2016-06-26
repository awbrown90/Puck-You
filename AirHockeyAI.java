import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.ArrayList;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;
import java.text.*;

public class AirHockeyAI
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
    
    //slots
    int smin = xmin+(xmax-xmin)/5;
    int smax = xmin+(xmax-xmin)*4/5;
    
    //Main Components
    private MovBall2 actor, target;
    
    //Actor properties
    private double max_speed, actor_x, actor_y, actor_r;
    private final int DELAY = 10; //milliseconds
    private double sp_coef = 1000/DELAY ;
    
    //Target properties aka puck
    private double puck_x, puck_y, puck_r;
    
    
    public AirHockeyAI(MovBall2 input_actor, MovBall2 input_target, double difficulty)
    {
        max_speed = difficulty;
        
        target = input_target;
        actor = input_actor;
        
        puck_r = target.getCircle().getRadius();
        actor_r = actor.getCircle().getRadius();
       
    }
    public void move()
    {
        //get target aka puck positions
       puck_x = target.getCircle().getX();
       puck_y = target.getCircle().getY();
       
       //get Actor positions
       actor_x = actor.getCircle().getX();
       actor_y = actor.getCircle().getY();
       
        //puck is on player side
       if(puck_y-puck_r > HEIGHT/2 )
       {
            double x_spot;
            if(puck_x-puck_r < smin)
            {
                 x_spot = smin + puck_r;
            }
            else if(puck_x+puck_r > smax)
            {
                 x_spot = smax - puck_r;
            }
            else
            {
                x_spot = puck_x;
            }
           Move_AI(max_speed,x_spot,HEIGHT/2-230,0); //Defense
       }
       //puck is on opponent side
       else
       {
           //puck is behind or to the side of opponent
          if(puck_y <= actor_y)
          {
              Move_AI(max_speed,WIDTH/2,actor_r,0); //Fall Back
          }
          //puck is infront of opponent
          else
          {
              double puck_angle = target.getAngle();
              //puck is not moving away from opponent
              if(puck_angle < 200 || puck_angle > 340)
              {
                  Move_AI(max_speed,puck_x,puck_y,0); //Offense
              }
              else//puck is moving away from opponent
              {
                   double x_spot;
                   if(puck_x-puck_r < smin)
                   {
                       x_spot = smin + puck_r;
                   }
                   else if(puck_x+puck_r > smax)
                   {
                       x_spot = smax - puck_r;
                   }
                   else
                   {
                       x_spot = puck_x;
                   }
                  Move_AI(max_speed,x_spot,HEIGHT/2-230,0); //Defense
              }
          }
       }  
    }
     public void Move_AI(double max_speed, double x_spot,double y_spot, double tolerance)
     {
           
           double desired_angle = actor.AngleTo(x_spot,y_spot);
        
           double x_diff = actor_x - x_spot;
           double y_diff = actor_y - y_spot;
           
           double target_dis = Math.sqrt(x_diff*x_diff+y_diff*y_diff);
           
           if(target_dis > tolerance )
           {
               double max_dis = max_speed/sp_coef;
               double desired_distance;
               if(target_dis >= max_dis)
               {
                   desired_distance = max_dis;
               }
               else
               {
                   desired_distance = target_dis;
               }
                actor.setSpeed(desired_distance * sp_coef);
                double delta_x = desired_distance * Math.cos(desired_angle);
                double delta_y = desired_distance * Math.sin(desired_angle);
           
                actor.getCircle().setX(actor_x+delta_x);
                actor.getCircle().setY(actor_y-delta_y);
                actor.setAngle(180/Math.PI*desired_angle);
            }
            else
            {
                actor.setSpeed(0);
            }
            actor.OverlapAvoid();
    } 
    public void setDifficulty(double difficulty)
    {
        max_speed = difficulty;
    }

}

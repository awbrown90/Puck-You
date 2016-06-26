import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.ArrayList;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;
import java.text.*;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;

public class PuckOff extends JPanel
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
    double smin = xmin+(xmax-xmin)/5;
    double smax = xmin+(xmax-xmin)*4/5;
    
    double x_lim_min = xmin;
    double x_lim_max = xmax;
    
    //Game pieces
    private static MovBall2 player, puck, opponent;
    
    private AirHockeyAI opponent_AI;
    public BallController player_control;
    
    //Graphics
    BufferedImage table,
    goal_img, goal_mask, goal,
    goal_opp_img, goal_opp_mask, goal_opp,
    winner_img, winner_mask, winner,
    winner_opp_img, winner_opp_mask, winner_opp,
    loser_img, loser_mask, loser,
    loser_opp_img, loser_opp_mask, loser_opp,
    zero_img, zero_mask, zero,
    one_img, one_mask, one,
    two_img, two_mask, two,
    three_img, three_mask, three,
    four_img, four_mask, four,
    five_img, five_mask, five,
    six_img, six_mask, six,
    seven_img, seven_mask, seven;
    
    //Score
    private int num_score_player;
    private int num_score_opponent;
    
    //Display
    private final int DELAY = 10; //milliseconds
    
    private int dis_timer = 0;
    //switch values (tristate)
    private int Display_Goal = 0;
    private int Display_Result = 0;
    
    //actual times
    private int goal_time = 1000; // milliseconds
    private int results_time = 2000; // milliseconds
    //converted times
    private int dis_goal_time = goal_time/DELAY; //converted display time for the goal screen
    private int dis_results_time = results_time/DELAY; //converted display time for the results screen
    
    //Store score graphics
    private ArrayList<BufferedImage> score_num = new ArrayList<BufferedImage>();
    
    //Reset Positions
    private int reset_player_x = WIDTH/2;
    private int reset_player_y = HEIGHT/2+230;
    
    private int reset_opponent_x = WIDTH/2;
    private int reset_opponent_y = HEIGHT/2-230;
    
    private int reset_puck_x = WIDTH/2;
    //top puck position
    private int reset_puck_y1 = HEIGHT/2-120;
    //bottom puck position
    private int reset_puck_y2 = HEIGHT/2+120;
    
    public PuckOff(String face)
    {
        Circle circle_player = new Circle(32,Color.red, WIDTH/2 , HEIGHT/2+230,1.18, "blue");
        player = new MovBall2(circle_player, 0, 90, false);
        
        Circle circle_puck = new Circle(26,Color.black,WIDTH/2, HEIGHT/2+120,1.5, face);
        puck = new MovBall2(circle_puck, 30, 90, true);
        
        puck.setFric(.4);
        puck.setMnsp(30);
        
        Circle circle_opp = new Circle(32,Color.red, WIDTH/2 , HEIGHT/2-230,1.18, "red");
        opponent = new MovBall2(circle_opp, 200, 270, false);
        
        opponent_AI = new AirHockeyAI(opponent,puck,200);
        player_control = new BallController(player);
        
        puck.AddCollision(player);
        puck.AddCollision(opponent);
        
        player.AddCollision(puck);
        opponent.AddCollision(puck);
        
        //intialize scores
        num_score_player = 0;
        num_score_opponent = 0;
        
        LoadImages();
        
        score_num.add(zero);
        score_num.add(one);
        score_num.add(two);
        score_num.add(three);
        score_num.add(four);
        score_num.add(five);
        score_num.add(six);
        score_num.add(seven);
        
    }
    public static MovBall2 getPuck()
    {
        return puck;
    }
    public void setFace(String face)
    {
        puck.getCircle().Face(face);
    } 
    public void setDifficulty(int difficulty)
    {
        if(difficulty == 0)
        {
            opponent_AI.setDifficulty(200);
        }
        if(difficulty == 1)
        {
            opponent_AI.setDifficulty(400);
        }
        if(difficulty == 2)
        {
            opponent_AI.setDifficulty(600);
        }
        if(difficulty == 3)
        {
            opponent_AI.setDifficulty(800);
        }
    }
    public void LoadImages()
    {
        try {
           table = ImageIO.read(new File("textures/airhockey_table.jpg"));
           
           goal_img = ImageIO.read(new File("textures/goal.jpg"));
           goal_mask = ImageIO.read(new File("textures/goal_alpha.jpg"));
           
           goal_opp_img = ImageIO.read(new File("textures/goal_opp.jpg"));
           goal_opp_mask = ImageIO.read(new File("textures/goal_opp_alpha.jpg"));
           
           winner_img = ImageIO.read(new File("textures/winner.jpg"));
           winner_mask = ImageIO.read(new File("textures/winner_alpha.jpg"));
           
           winner_opp_img = ImageIO.read(new File("textures/winner_opp.jpg"));
           winner_opp_mask = ImageIO.read(new File("textures/winner_opp_alpha.jpg"));
           
           loser_img = ImageIO.read(new File("textures/loser.jpg"));
           loser_mask = ImageIO.read(new File("textures/loser_alpha.jpg"));
           
           loser_opp_img = ImageIO.read(new File("textures/loser_opp.jpg"));
           loser_opp_mask = ImageIO.read(new File("textures/loser_opp_alpha.jpg"));
            
           zero_img = ImageIO.read(new File("textures/score_0.jpg"));
           zero_mask = ImageIO.read(new File("textures/score_0_alpha.jpg"));
           
           one_img = ImageIO.read(new File("textures/score_1.jpg"));
           one_mask = ImageIO.read(new File("textures/score_1_alpha.jpg"));
           
           two_img = ImageIO.read(new File("textures/score_2.jpg"));
           two_mask = ImageIO.read(new File("textures/score_2_alpha.jpg"));
           
           three_img = ImageIO.read(new File("textures/score_3.jpg"));
           three_mask = ImageIO.read(new File("textures/score_3_alpha.jpg"));
           
           four_img = ImageIO.read(new File("textures/score_4.jpg"));
           four_mask = ImageIO.read(new File("textures/score_4_alpha.jpg"));
           
           five_img = ImageIO.read(new File("textures/score_5.jpg"));
           five_mask = ImageIO.read(new File("textures/score_5_alpha.jpg"));
           
           six_img = ImageIO.read(new File("textures/score_6.jpg"));
           six_mask = ImageIO.read(new File("textures/score_6_alpha.jpg"));
           
           seven_img = ImageIO.read(new File("textures/score_7.jpg"));
           seven_mask = ImageIO.read(new File("textures/score_7_alpha.jpg"));
       } catch (IOException e) {
       }
        
       goal = GraphicsLab.AlphaMask(goal_img,goal_mask);
       goal_opp = GraphicsLab.AlphaMask(goal_opp_img,goal_opp_mask);
       winner = GraphicsLab.AlphaMask(winner_img,winner_mask);
       winner_opp = GraphicsLab.AlphaMask(winner_opp_img,winner_opp_mask);
       loser = GraphicsLab.AlphaMask(loser_img,loser_mask);
       loser_opp = GraphicsLab.AlphaMask(loser_opp_img,loser_opp_mask);
       zero = GraphicsLab.AlphaMask(zero_img,zero_mask);
       one = GraphicsLab.AlphaMask(one_img,one_mask);
       two = GraphicsLab.AlphaMask(two_img,two_mask);
       three = GraphicsLab.AlphaMask(three_img,three_mask);
       four = GraphicsLab.AlphaMask(four_img,four_mask);
       five = GraphicsLab.AlphaMask(five_img,five_mask);
       six = GraphicsLab.AlphaMask(six_img,six_mask);
       seven = GraphicsLab.AlphaMask(seven_img,seven_mask);
         
    }
    public void MovePuck()
    {
        //move puck on its on to watch for collisons with player and opponent
        puck.Move();
        //then move puck and watch for boundaries
        //call Score when puck gets too high or too low
        
        //get puck attributes
        int radius = puck.getCircle().getRadius();
        double pos_x = puck.getCircle().getX();
        double pos_y = puck.getCircle().getY();
        
        //Check for Score
        //if player scored and not currently displaying
        if( (pos_y < ymin-2*radius) && (Display_Goal==0) && (Display_Result==0))
        {
            Score(1);
        }
        else if( (pos_y > ymax+2*radius) && (Display_Goal==0) && (Display_Result==0))
        {
            Score(2);
        }
        else
        {
        
            //Watch for X boundary
            if ( pos_x <= x_lim_min + radius || pos_x >= x_lim_max - radius )
            {
                puck.setAngle(180 - puck.getAngle());
                
                if( pos_x < xmin + radius )
                {
                    puck.getCircle().setX(xmin+radius);
                }
                else if( pos_x > xmax - radius )
                {
                    puck.getCircle().setX(xmax-radius);
                }
            }
            //Watch for Y boundary including goal slots
            if ( pos_y <= ymin + radius || pos_y >=  ymax - radius )
            {
                //puck is not inside slot
                if( pos_x <= smin || pos_x >= smax  )
                {
                       puck.setAngle(-puck.getAngle());
                       
                       if( pos_y < ymin + radius )
                       {
                           puck.getCircle().setY(ymin + radius);
                       }
                       else if( pos_y > ymax - radius )
                       {
                           puck.getCircle().setY(ymax - radius);
                       }
                }
                //if puck is touching slot corners (also includes rebounding off inner slot walls)
                else if( pos_x < smin+radius || pos_x > smax-radius)
                {
                    //if top slot corners
                   if(pos_y < ymin + radius)
                   {
                       //could be touching top left slot corner
                       if(pos_x < smax-radius)
                       {
                           //check if colliding with top left slot corner 
                           SlotBoundary(smin,ymin,puck);
                           
                        }
                        //could be touching top right slot corner
                        else 
                        {
                           //check if colliding with top right corner position
                           SlotBoundary(smax,ymin,puck);
                           
                        }
                    }
                    //if bottom slot corners
                    else
                    {
                       //could be touching bottom left slot corner
                       if(pos_x < smax -radius)
                       {
                           //check if colliding with bottom left corner position
                           SlotBoundary(smin,ymax,puck);
                           
                        }
                        //could be touching bottom right slot corner
                        else 
                        {
                           //check if colliding with bottom right corner position
                           SlotBoundary(smax,ymax,puck);
                           
                        }
                    }
                }
                //puck is inside the goal slot
                else if(pos_y <= ymin || pos_y >= ymax)
                {
                    x_lim_min = smin;
                    x_lim_max = smax;
                }
                  
            }
            else
            {
                x_lim_min = xmin;
                x_lim_max = xmax;
            }
        }
        
    }
    public void SlotBoundary(double cornX, double cornY, MovBall2 ball)
    {
           double pos_x = ball.getCircle().getX();
           double pos_y = ball.getCircle().getY();
           int radius = ball.getCircle().getRadius();
           
           //if touching corner point
           if(Math.sqrt( (cornX-pos_x)*(cornX-pos_x) + (cornY-pos_y)*(cornY-pos_y) ) <= radius )
           {
              double ref_angle = ball.AngleAway(cornX,cornY);
              double delta_x = radius*Math.cos(ref_angle);
              double delta_y = -radius*Math.sin(ref_angle);
              
              //reposition to touch corner
              ball.getCircle().setX(delta_x+cornX);
              ball.getCircle().setY(delta_y+cornY);
              
              double reflect_angle = ball.AngleTo(cornX,cornY);
              double delta_angle = (Math.PI+Math.toRadians(ball.getAngle() )) - reflect_angle;
              
              //change angle to rebound angle
              ball.setAngle(Math.toDegrees(reflect_angle - delta_angle));
             
            }
            
    }
    public void Move()
    {
        //move player
        player_control.move();
        //move puck if not currently displaying goal or result
        if( (Display_Goal==0) && (Display_Result==0) )
        {
            MovePuck();
        }
        else
        {
            dis_timer++; //progress Display if active
        }
            
        //move opponent
        opponent_AI.move();
        
    } 
    public void Respawn(int puckside)
    {
      
           //reset speeds
           puck.setSpeed(0);
           player.setSpeed(0);
           opponent.setSpeed(0);
           
           //set puck orientation, matters for AI
           puck.setAngle(90);
       
           //set positions
           player.getCircle().setX(reset_player_x);
           opponent.getCircle().setX(reset_opponent_x);
           puck.getCircle().setX(reset_puck_x);
       
           player.getCircle().setY(reset_player_y);
           opponent.getCircle().setY(reset_opponent_y);
           
           // puckside = 1 is top, puckside = 2 is bottom for setting up puck
           //eg. if puckside 1 then player scored and puck is set to top
           if (puckside == 1)
           {
               //respawn puck at top
               puck.getCircle().setY(reset_puck_y1);
           
           }
           if (puckside == 2)
           {
                //respawn puck at bottom
                puck.getCircle().setY(reset_puck_y2);
            
           }
           
    }
    //if score = 1, player scored if score = 2, opponent scored
    public void Score(int score)
    {
        //check if last point
        boolean last_point =  (num_score_player==6 && score == 1) || (num_score_opponent==6 && score == 2);
        
        if(score == 1)
        {
            num_score_player++;
        }
        if(score == 2)
        {
            num_score_opponent++;
        }
        
        if(last_point)
        {
            Display_Result = score;
        }
        else
        {
            Display_Goal = score;
        }
        
    }
    public void Reset()
    {
        num_score_player=0;
        num_score_opponent=0;
        Display_Goal=0;
        Display_Result=0;
        player_control.ResetPoint();
        Respawn(2);
    }
    public void paint_game(Graphics page)
    {
       page.drawImage(table,0,0,null);
       
       //draw slots
       page.drawLine((int)smin,ymin,(int)smin,ymin-26);
       page.drawLine((int)smax,ymin,(int)smax,ymin-26);
             
       page.drawLine((int)smin,ymax,(int)smin,ymax+26);
       page.drawLine((int)smax,ymax,(int)smax,ymax+26);
             
       page.drawImage(score_num.get(num_score_opponent),WIDTH/2+90,HEIGHT/2-70,null);
       page.drawImage(score_num.get(num_score_player),WIDTH/2+90,HEIGHT/2+10,null);
       
       player.draw(page);
       opponent.draw(page);
       
       //check if display goal was switched
       if(Display_Goal!=0)
       {
           Display_Goal(page);
       }
       //check if display result was switched
       else if(Display_Result!=0)
       {
           Display_Result(page);
       }
       else
       {
           //if not currently displaying goal or result
          puck.draw(page);
       }
       
       //debug messages
        //page.setColor(Color.white);
        //page.drawString("puck speed: "+Double.toString(puck.getSpeed()),WIDTH/2+20,HEIGHT/2+100); 
        //page.drawString("player speed: "+Double.toString(player.getSpeed()),WIDTH/2+20,HEIGHT/2+150); 
        //page.drawString("opponent speed: "+Double.toString(opponent.getSpeed()),WIDTH/2+20,HEIGHT/2+200);
        
       
    }
    public void Display_Goal(Graphics g)
    {
       if(dis_timer >= dis_goal_time)
       {
           
           //Display Action: respawn the players, and puck
           Respawn(Display_Goal);
           
           //reset
           Display_Goal=0;
           dis_timer=0;
        }
        else
        {
             //Display goal image
             //player scored
             if(Display_Goal == 1)
             {
                 g.drawImage(goal,WIDTH/2-60,HEIGHT/2+60,null);
             }
             //opponent scored
             else if(Display_Goal == 2)
             {
                 g.drawImage(goal_opp,WIDTH/2-60,HEIGHT/2-180,null);
             }
             
        }
    }
    public void Display_Result(Graphics g)
    {
       if(dis_timer >= dis_results_time)
       {
           
           //Display Action: respawn the players, and puck
           
           //make it so the player always starts with puck when resetting game
           Reset();
           
           //reset
           Display_Result=0;
           dis_timer=0;
        }
        else
        {
             //Display result image
             //player won
             if(Display_Result == 1)
             {
                 g.drawImage(winner,WIDTH/2-100,HEIGHT/2+60,null);
                 g.drawImage(loser_opp,WIDTH/2-100,HEIGHT/2-180,null);
             }
             //opponent won
             else if(Display_Result == 2)
             {
                 g.drawImage(winner_opp,WIDTH/2-100,HEIGHT/2-180,null);
                 g.drawImage(loser,WIDTH/2-100,HEIGHT/2+60,null);
             }
             
        }
    }
    
}

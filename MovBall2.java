import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.awt.Point;

public class MovBall2
{
    private Circle circle;
    public double moveX, moveY, angle;
    public double speed; // in the units of pixels per second
    private double slow_down;
    private boolean just_hit;
    //.693
    // the update time for the ball in the gui
    private final int DELAY = 10; //milliseconds
    //speed coef used to get speed in pixels/sec
    private double sp_coef = 1000/DELAY ;
    private double Coef_fric;
    private double fric_t;
    
    //use friction for move function
    private boolean friction;
    
    private ArrayList<MovBall2> Collision = new ArrayList<MovBall2>();
    private boolean slot;
    private boolean debug_angle;
    
    private double max_speed;
    private double min_speed;
    private double strike_strength;
    
    private boolean event;
    
    //to keep track of scoring
    // 0 - no score
    // 1 - player scored
    // 2 - opponent scored
    private int score;
    
    private int radius;
    private double cos_angle;
    private double sin_angle;
    
    private double fric_increment;
    
    public MovBall2(Circle circle, int speed, double angle, boolean friction)
    {
        this.circle = circle;
        this.speed = speed;
        slow_down = speed;
        
        this.angle = angle;
       
        this.friction = friction;
        
        Coef_fric = 0;
        max_speed = 1200;
        min_speed = 0;
        strike_strength = 1;
        
        debug_angle = false;
        
        just_hit = false;
        moveX =    speed * Math.cos( Math.toRadians(angle) ) /sp_coef;
        moveY =    speed * Math.sin( Math.toRadians(angle) ) /sp_coef;
        fric_t = 0;
        
        radius = circle.getRadius();
        cos_angle = Math.cos(  Math.toRadians(angle) ) /sp_coef;
        sin_angle = Math.sin(  Math.toRadians(angle) ) /sp_coef ;
        
        fric_increment = (double)DELAY/1000;
        
        event = false;
        
    }
   public void AddCollision(MovBall2 Model)
   {
       Collision.add(Model);
    }
    public Circle getCircle()
    {
        return circle;
    }
    public void setRadius(int size)
    {
        radius = size;
        circle.setRadius(size);
    }
    public void setRatio(double ratio)
    {
        circle.setRatio(ratio);
    }
    public void setAngle(double angle)
    {
        
        this.angle = angle%360;
        cos_angle = Math.cos( Math.toRadians(angle) ) /sp_coef ;
        sin_angle = Math.sin( Math.toRadians(angle) ) /sp_coef ;
        
        moveX =    speed * cos_angle;
        moveY =    speed * sin_angle;
    }
    public double getAngle()
    {
        return angle;
    }
    //return the angle needed to go to get away from the ball entered in parameter
    public double AngleAway(Circle target)
    {
         double VecX = circle.getX() - target.getX();
         double VecY = target.getY() -  circle.getY();
         if ( VecY >0)
         {
            return Math.acos( VecX / Math.sqrt( (VecX * VecX) + (VecY * VecY) ) )  ;
         }
         else
         {
            return ((2*Math.PI) - Math.acos( VecX / Math.sqrt( (VecX * VecX) + (VecY * VecY) ) )) ;
         }  
    }
    public double AngleAway(double x, double y)
    {
      double VecX = circle.getX() - x;
         double VecY = y -  circle.getY();
         if ( VecY >0)
         {
            return  Math.acos( VecX / Math.sqrt( (VecX * VecX) + (VecY * VecY) ) )  ;
         }
         else
         {
            return ((2*Math.PI) - Math.acos( VecX / Math.sqrt( (VecX * VecX) + (VecY * VecY) ) )) ;
         }  
    }  
    public double AngleAway(double refx, double refy, double tarx, double tary)
    {
         double VecX = refx - tarx;
         double VecY = tary -  refy;
         if ( VecY >0)
         {
            return Math.acos( VecX / Math.sqrt( (VecX * VecX) + (VecY * VecY) ) )  ;
         }
         else
         {
            return ((2*Math.PI) - Math.acos( VecX / Math.sqrt( (VecX * VecX) + (VecY * VecY) ) )) ;
         }  
    }  
    public double AngleTo(double refx, double refy, double tarx, double tary)
    {
        double ref_angle = AngleAway(refx,refy,tarx,tary);
        if(ref_angle < Math.PI)
        {
            return ref_angle + Math.PI;
        }
        else
        {
           return ref_angle - Math.PI; 
        }
    }
    //return the angle needed to get to the from the ball entered in parameter
    public double AngleTo(Circle target)
    {
        double ref_angle = AngleAway(target);
        if(ref_angle < Math.PI)
        {
            return ref_angle + Math.PI;
        }
        else
        {
           return ref_angle - Math.PI; 
        }
    }
    public double AngleTo(double x, double y)
    {
        double ref_angle = AngleAway(x,y);
        if(ref_angle < Math.PI)
        {
            return ref_angle + Math.PI;
        }
        else
        {
           return ref_angle - Math.PI; 
        }
    } 
    // set game option parameters
    public void setFric(double input_fric)
    {
        Coef_fric = input_fric;
    }
    public void setMxsp(double input_speed)
    {
        max_speed = input_speed;
    }
    public void setMnsp(double input_speed)
    {
        min_speed = input_speed;
    }
    public void setSS(double input_strength)
    {
        strike_strength = input_strength;
    }
    
    public void setSpeed(double set_speed)
    {
        speed = set_speed;
        fric_t = 0;
        moveX =    speed * cos_angle ;
        moveY =    speed * sin_angle ;
    }
    public double getSpeed()
    {
        if(friction)
        {
            return slow_down;
        }
        
        else
        {
            return speed;
        }
    }
    public void Debug_Angle()
    {
        debug_angle = true;
    }
    public void setEvent()
    {
        event = true;
    }
    public void cleanEvent()
    {
        event = false;
    }
    public boolean getEvent()
    {
        return event;
    }
   public void draw(Graphics page)
   {
       circle.draw(page);
   }
   public void Damping(double speed)
   {
        
       slow_down = speed*Math.exp(-Coef_fric * fric_t);
       
       if(slow_down > min_speed)
       {
           moveX =    slow_down * cos_angle ;
           moveY =    slow_down * sin_angle ;
           fric_t += fric_increment; 
      }
      else
      {
          moveX =    min_speed * cos_angle ;
          moveY =    min_speed * sin_angle ;
      }
          
      
   } 
   public boolean Contains(double x, double y)
   {
       double diff_x = circle.getX()-x;
       double diff_y = circle.getY()-y;
       
       return (Math.sqrt(diff_x*diff_x+diff_y*diff_y) < radius);
       
   }
   public void OverlapAvoid()
   {
       double obs_x = circle.getX();
       double obs_y = circle.getY();
       
       for(int i = 0; i < Collision.size(); i++)
       {
            Circle ref_circle = Collision.get(i).getCircle();
            double ref_x = ref_circle.getX();
            double ref_y = ref_circle.getY();
            int ref_radius = ref_circle.getRadius();
            
            double diff_x = obs_x-ref_x;
            double diff_y = obs_y-ref_y;
            int proximity = radius + ref_radius;
            
            if(diff_x <= proximity)
            {
                if(diff_y <= proximity)
                {
                
                    double distance = Math.sqrt( Math.pow( (diff_x  ) ,2 ) + Math.pow( (diff_y ) ,2 ) );
                    int wall_tol = 2; //pixels
                    boolean touching_corner = (ref_x-ref_radius <= (7+wall_tol) || ref_x+ref_radius >= (313-wall_tol) )&&(ref_y-ref_radius <= (7+wall_tol) || ref_y+ref_radius >= (559-wall_tol) );
                    
                    
                    
                    if ( distance < (proximity ) && (touching_corner || Collision.get(i).getEvent() )   )
                    {
                        //ball Overlap Avoid
                        //reference angle is from collision ball to this ball
                        double ref_angle = Collision.get(i).AngleTo(circle);
                        double deltaX = (proximity)*Math.cos(ref_angle);
                        double deltaY = -(proximity)*Math.sin(ref_angle);
                        double set_x = ref_x+deltaX;
                        double set_y = ref_y+deltaY;
                        
                        circle.setX(set_x);
                        circle.setY(set_y);
                        
                        Collision.get(i).setSpeed(30);
                    }
                }
            }
        }  
    
        
   }
       
   public void Move()
   {
       
       if(friction)
       {
           Damping(speed);
       }
       
       double new_x = circle.getX() + moveX;
       double new_y = circle.getY() - moveY;
       
            for(int i = 0; i < Collision.size(); i++)
            {
                Circle ref_circle = Collision.get(i).getCircle();
                double x = ref_circle.getX();
                double y = ref_circle.getY();
                double diff_x = x-new_x;
                double diff_y = y-new_y;
                
                int ref_radius = ref_circle.getRadius();
                int proximity = radius + ref_radius;
                
                if(diff_x <= proximity)
                {
                    if(diff_y <= proximity)
                    {
                        double distance = Math.sqrt( Math.pow( (diff_x  ) ,2 ) + Math.pow( (diff_y ) ,2 ) );
                        if ( distance < (proximity ) )
                        {
                            //ball Overlap Avoid
                            //reference angle is from collision ball to this ball
                            double ref_angle = Collision.get(i).AngleTo(new_x,new_y);
                            double deltaX = (proximity)*Math.cos(ref_angle);
                            double deltaY = -(proximity)*Math.sin(ref_angle);
                            new_x = x+deltaX;
                            new_y = y+deltaY; 
                            
                            
                            setAngle(180/Math.PI*AngleAway(new_x, new_y, x, y));
                            double refAngle = Collision.get(i).getAngle();
                            double ref_speed = Collision.get(i).getSpeed();
                            double diff_angle;
                            if(angle != -1)
                            {
                                if(refAngle > angle)
                                {
                                    diff_angle = Math.PI/180*(refAngle-angle);
                                    if(diff_angle < Math.PI/2)
                                    {
                                        ref_speed = ref_speed*Math.cos(diff_angle);
                                    }
                                    else
                                    {
                                        ref_speed = 0;
                                    }
                                }
                                else if(refAngle < angle)
                                {
                                    diff_angle = Math.PI/180*(angle-refAngle);
                                    if(diff_angle < Math.PI/2)
                                    {
                                        ref_speed = ref_speed*Math.cos(diff_angle);
                                    }
                                    else
                                    {
                                        ref_speed = 0;
                                    }
                                }
                                
                            }
                            
                            ref_speed *= strike_strength;
                            
                            if(ref_speed > slow_down )
                            {
                                fric_t = 0;
                  
                                if(ref_speed < max_speed)
                                {
                                    speed = ref_speed;
                                }
                                else
                                {
                                     speed = max_speed;
                                }
                                
                            }
                            
                            moveX =    speed * cos_angle ;
                            moveY =    speed * sin_angle ;
                          //Collision.get(i).getCircle().setX(x+radius+circle.getRadius()*Math.cos(angle));
                          //Collision.get(i).getCircle().setY(y+radius+circle.getRadius()*Math.sin(angle));
                                  
                         }
                   }
               }
            }
      
           circle.setX( new_x);
           circle.setY( new_y);  
      
    }
   
}

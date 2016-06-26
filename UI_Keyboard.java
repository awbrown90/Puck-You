import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import java.util.ArrayList;

public class UI_Keyboard
{
    private ArrayList<DisplayButton> buttons = new ArrayList<DisplayButton>();
    private int key_width = 27 ; //pixels
    private int key_height = 38 ; //pixels
    private int key_spacing = 6; //pixels
    private int spacing_1 = 3; // pixels
    private int spacing_2 = 19; // pixels
    private int spacing_3 = 51; // pixels
    
    private String file_name ="";
    private String[] letters_1 = {"q","w","e","r","t","y","u","i","o","p"};
    private String[] letters_2 = {"a","s","d","f","g","h","j","k","l"};
    private String[] letters_3 = {"z","x","c","v","b","n","m"};
    
    boolean enter = false;
    
    public UI_Keyboard()
    {
        
        
        for(int i = 0; i < 10; i++)
        {
            buttons.add(new DisplayButton(key_width,key_height,spacing_1+i*(key_width+key_spacing),364));
        }
        for(int i = 0; i < 9; i++)
        {
            buttons.add(new DisplayButton(key_width,key_height,spacing_2+i*(key_width+key_spacing),418));
        }
        for(int i = 0; i < 7; i++)
        {
            buttons.add(new DisplayButton(key_width,key_height,spacing_3+i*(key_width+key_spacing),472));
        }
        //space button
        buttons.add(new DisplayButton(75,38,83,526));
        //enter button
        buttons.add(new DisplayButton(75,38,243,526));
        //back button
        buttons.add(new DisplayButton(36,38,281,472));
        
    }
    public void CheckKey(int x, int y)
    {
        for(int i = 0; i < 29; i++)
        {
            if(buttons.get(i).Contains(x,y))
            {
                if(i < 10)
                {
                    file_name += letters_1[i];
                }
                else if( i < 19)
                {
                    file_name += letters_2[i-10];
                }
                else if( i < 26)
                {
                    file_name += letters_3[i-19];
                }
                else if( i == 26 )
                {
                    file_name += " ";
                }
                else if( i == 27 )
                {
                    enter = true;
                }
                else if( i == 28 )
                {
                    file_name = file_name.substring(0,file_name.length()-1);
                }
               
            }
        }
    }
    public boolean CheckEnter()
    {
        boolean output = enter;
        enter = false;
        return output;
    }
    public String getName()
    {
        return file_name;
    }
    public void resetName()
    {
       file_name =""; 
    }
}

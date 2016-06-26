import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

public class DisplayButton
{
    private int button_width, button_height, button_xoffset, button_yoffset;
    
    public DisplayButton(int width, int height, int x, int y)
    {
        button_width = width;
        button_height = height;
        button_xoffset = x;
        button_yoffset = y;
    }
    boolean Contains(int x_point, int y_point)
    {
        return ( (x_point > button_xoffset && x_point < (button_width+button_xoffset))&&(y_point > button_yoffset && y_point < (button_height+button_yoffset)) );
    }
}
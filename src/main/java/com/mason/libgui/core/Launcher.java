
package com.mason.libgui.core;

import com.mason.libgui.components.*;
import com.mason.libgui.utils.UIAligner;

import java.awt.Color;
import java.awt.Graphics2D;

import static com.mason.libgui.utils.StyleInfo.ALPHA_STYLE_INFO;
import static com.mason.libgui.utils.StyleInfo.DEFAULT_STYLE_INFO;

/**
 *
 * @author Adam Whittaker
 */
public class Launcher{
    
    
    public static final int WIDTH = 800, HEIGHT = 400;
    public static final GUIManager gui = new GUIManager(WIDTH, HEIGHT, "Boo");
    
    
    public static void main(String[] args){
        new Thread(new Pacemaker(gui)).start();
        
        Pane pane = new SlidingPane(120, 16, 400, 300, UIAligner.Direction.RIGHT, 21, WIDTH, HEIGHT);
        
        Button b = new Toggle(DEFAULT_STYLE_INFO, 50, 50, 64, 64);
        UIText text = new Tooltip("The text", ALPHA_STYLE_INFO, 150, 150, 75, 32);
        
        DraggableComponent d = new DraggableComponent(50, 50, 64, 64){
            
            public void render(Graphics2D g){
                g.setColor(Color.ORANGE);
                g.fillRect(x, y, width, height);
            }
            
        };
        pane.addComponent(b);
        
        SmoothSlider s = SmoothSlider.getDefaultSlider(310, 130, 150, false);
        pane.addComponent(s);
        pane.addComponent(text);
        gui.addComponent(pane);
        gui.addComponent(d);
    }
    
}

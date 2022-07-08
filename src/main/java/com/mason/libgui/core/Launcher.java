
package com.mason.libgui.core;

import com.mason.libgui.components.*;
import java.awt.Color;
import java.awt.Graphics2D;

import static com.mason.libgui.utils.ColorScheme.DEFAULT_COLOR_SCHEME;

/**
 *
 * @author Adam Whittaker
 */
public class Launcher{
    
    
    public static final int WIDTH = 800, HEIGHT = 400;
    public static final GUIManager gui = new GUIManager(WIDTH, HEIGHT, "Boo");
    
    
    public static void main(String[] args){
        new Thread(new Pacemaker(gui)).start();
        
        Pane pane = new DraggablePane(120, 16, 400, 300);
        
        Button b = new Toggle(DEFAULT_COLOR_SCHEME, 50, 50, 64, 64);
        DraggableComponent d = new DraggableComponent(50, 50, 64, 64){
            
            public void render(Graphics2D g){
                g.setColor(Color.ORANGE);
                g.fillRect(x, y, width, height);
            }
            
        };
        pane.addComponent(b);
        
        SmoothSlider s = SmoothSlider.getDefaultSlider(310, 130, 150, false);
        pane.addComponent(s);
        gui.addComponent(pane);
        gui.addComponent(d);
    }
    
}


package com.mason.libgui.core;

import com.mason.libgui.components.keyInput.KeyBuffer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

/**
 *
 * @author Adam Whittaker
 */
public class Window extends JFrame{
    
    
    private final Canvas canvas = new Canvas();
    public static final Color BACKGROUND_COLOR = new Color(20,20,20);


    public Window(int width, int height, String title, UIComponentManager cm, KeyBuffer keys, Image toolbarImg, Cursor cursor){
        this(width, height, title, cm, keys);

        setIconImage(toolbarImg);
        setCursor(cursor);
    }
    
    public Window(int width, int height, String title, UIComponentManager cm, KeyBuffer keys){
        super(title);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        
        canvas.setPreferredSize(new Dimension(width, height));
        canvas.setFocusable(false);
        add(canvas);
        pack();
        setLocationRelativeTo(null);

        int NUM_BUFFERS = 4;
        canvas.createBufferStrategy(NUM_BUFFERS);
        canvas.addMouseListener(cm);
        canvas.addMouseMotionListener(cm);
        canvas.addMouseWheelListener(cm);
        this.addKeyListener(keys);
        
        setVisible(true);
    }
    
    
    protected void render(UIComponentManager compManager){
        BufferStrategy bs = canvas.getBufferStrategy();
        Graphics2D bsg = (Graphics2D) bs.getDrawGraphics();
        
        bsg.setColor(BACKGROUND_COLOR);
        bsg.fillRect(0, 0, getWidth(), getHeight());
        
        compManager.render(bsg);
        
        bsg.dispose();
        bs.show();
    }
    
}

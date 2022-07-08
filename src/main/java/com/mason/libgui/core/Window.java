
package com.mason.libgui.core;

import java.awt.*;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;

/**
 *
 * @author Adam Whittaker
 */
public class Window extends JFrame{
    
    
    private final Canvas canvas = new Canvas();
    public static final Color BACKGROUND_COLOR = new Color(20,20,20);
    private final int NUM_BUFFERS = 4;
    
    
    public Window(int width, int height, String title, ComponentManager cm, Image toolbarImg, Cursor cursor){
        this(width, height, title, cm);

        setIconImage(toolbarImg);
        setCursor(cursor);
    }
    
    public Window(int width, int height, String title, ComponentManager cm){
        super(title);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        
        canvas.setPreferredSize(new Dimension(Launcher.WIDTH, Launcher.HEIGHT));
        canvas.setFocusable(false);
        add(canvas);
        pack();
        
        canvas.createBufferStrategy(NUM_BUFFERS);
        canvas.addMouseListener(cm);
        canvas.addMouseMotionListener(cm);
        canvas.addMouseWheelListener(cm);
        
        setVisible(true);
    }
    
    
    protected void render(ComponentManager compManager){
        BufferStrategy bs = canvas.getBufferStrategy();
        Graphics2D bsg = (Graphics2D) bs.getDrawGraphics();
        
        bsg.setColor(BACKGROUND_COLOR);
        bsg.fillRect(0, 0, Launcher.WIDTH, Launcher.HEIGHT);
        
        compManager.render(bsg);
        
        bsg.dispose();
        bs.show();
    }
    
}

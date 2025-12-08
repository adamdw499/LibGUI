
package com.mason.libgui.core.guiManagement;

import com.mason.libgui.core.componentManagement.UIComponentManager;
import com.mason.libgui.core.input.rawLayer.RawInputSocket;
import com.mason.libgui.utils.structures.Size;

import javax.swing.*;
import java.awt.*;


public class Window{


    private final CanvasRenderer renderer;
    private final JFrame frame;

    
    Window(Size frameSize, String title){
        frame = new JFrame(title);
        setupFrame();
        renderer = new CanvasRenderer(frameSize);
        renderer.setUpWithFrame(frame);
        frame.pack();
    }

    private void setupFrame(){
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


    void registerInputSocket(RawInputSocket processor){
        renderer.registerInputSocket(processor);
        frame.addKeyListener(processor);
    }

    void setToolbarImage(Image toolbarImg){
        frame.setIconImage(toolbarImg);
    }

    void setCursor(Cursor cursor){
        frame.setCursor(cursor);
    }
    
    
    void render(UIComponentManager compManager){
        renderer.render(compManager);
    }
    
}

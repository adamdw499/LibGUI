package com.mason.libgui.core.guiManagement;

import com.mason.libgui.core.componentManagement.UIComponentManager;
import com.mason.libgui.core.input.rawLayer.RawInputSocket;
import com.mason.libgui.utils.structures.Size;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class CanvasRenderer{


    public static final Color BACKGROUND_COLOR = new Color(20,20,20);
    private static final int NUM_BUFFERS = 4;
    private final Canvas canvas;
    private final Size frameSize;


    CanvasRenderer(Size frameSize){
        canvas = new Canvas();
        this.frameSize = frameSize;
        canvas.setPreferredSize(new Dimension(frameSize.width(), frameSize.height()));
        canvas.setFocusable(false);
    }


    void setUpWithFrame(JFrame frame){
        frame.add(canvas);
        canvas.createBufferStrategy(NUM_BUFFERS);
    }

    void registerInputSocket(RawInputSocket processor){
        canvas.addMouseListener(processor);
        canvas.addMouseMotionListener(processor);
        canvas.addMouseWheelListener(processor);
    }


    void render(UIComponentManager compManager){
        BufferStrategy bufferStrategy = canvas.getBufferStrategy();
        Graphics2D graphics = (Graphics2D) bufferStrategy.getDrawGraphics();

        paintBackground(graphics);
        compManager.renderComponents(graphics);

        graphics.dispose();
        bufferStrategy.show();
    }

    private void paintBackground(Graphics2D g){
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(0, 0, frameSize.width(), frameSize.height());
    }

}

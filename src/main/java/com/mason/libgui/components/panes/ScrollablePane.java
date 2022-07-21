package com.mason.libgui.components.panes;

import com.mason.libgui.components.draggables.Draggable;
import com.mason.libgui.components.draggables.DraggableComponent;
import com.mason.libgui.core.UIComponent;
import com.mason.libgui.utils.StyleInfo;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;

import static com.mason.libgui.utils.RenderUtils.drawBorder;

public class ScrollablePane extends Pane{


    private Camera camera;
    private static final double MAX_ZOOM = 8.0, MIN_ZOOM = 0.512;


    public ScrollablePane(int x, int y, int w, int h, int vx, int vy, int vw, int vh, boolean panLocked){
        super(x, y, w, h);
        setCamera(vx, vy, vw, vh, panLocked);
        addComponent(camera);
    }

    public ScrollablePane(int x, int y, int w, int h, int vw, int vh, boolean panLocked){
        this(x, y, w, h, (vw-w)/2, (vh-h)/2, vw, vh, panLocked);
    }


    public boolean withinBounds(int mx, int my){
        return withinBounds(x, y, camera.getWidth(), camera.getHeight(), mx, my);
    }

    protected boolean inView(UIComponent comp){
        return camera.intersects(comp);
    }

    public void setCamera(int vx, int vy, int vw, int vh, boolean panLocked){
        camera = new Camera(vx, vy, vw, vh, panLocked);
    }


    @Override
    protected void renderBorder(Graphics2D g){
        drawBorder(g, StyleInfo.DEFAULT_STYLE_INFO, x, y, camera.getWidth(), camera.getHeight());
    }

    @Override
    protected void renderComponents(Graphics2D g){
        g.setClip(x, y, camera.getWidth(), camera.getHeight());
        AffineTransform saved = g.getTransform();
        g.transform(AffineTransform.getTranslateInstance(x + camera.getX(), y + camera.getY()));
        g.transform(AffineTransform.getScaleInstance(camera.zoom, camera.zoom));
        for(int n = components.size()-1; n>=0; n--){
            /*if(inView(components.get(n))) */components.get(n).render(g);
        }
        g.setTransform(saved);
        g.setClip(null);
    }

    @Override
    public void mousePressed(MouseEvent e){
        super.mousePressed(e);
        if(!isDragging()){
            setDragging(camera);
            e = relativeMouseCoords(e);
            camera.mousePressed(e);
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e){
        super.mouseWheelMoved(e);
        camera.mouseWheelMoved(e);
    }


    protected class Camera extends DraggableComponent{


        private final boolean panLocked;
        private double zoom = 1.0;


        public Camera(int x, int y, int w, int h, boolean panLocked){
            super(x, y, w, h);
            this.panLocked = panLocked;
        }


        @Override
        public void mouseDragged(MouseEvent e){
            if(dragging){
                x = (int)(e.getX()/zoom) - diffX;
                y = (int)(e.getY()/zoom) - diffY;
            }
            if(panLocked){
                if(x < width-ScrollablePane.this.width) x = width-ScrollablePane.this.width;
                else if(x > 0) x = 0;
                if(y > 0) y = 0;
                else if(y < height-ScrollablePane.this.height) y = height-ScrollablePane.this.height;
            }
        }


        @Override
        public void mouseWheelMoved(MouseWheelEvent me){
            me = relativeMouseCoords(me);
            double x = -camera.getX()-me.getX()/zoom, y = -camera.getY()-me.getY()/zoom;
            if (me.getWheelRotation() < 0) {
                if (zoom < MAX_ZOOM) {
                    zoom *= 1.25;
                    camera.setX((int)(x-me.getX()/zoom));
                    camera.setY((int)(y-me.getY()/zoom));
                }
            }else{
                if (zoom > MIN_ZOOM) {
                    zoom /= 1.25;
                    camera.setX((int)(x-me.getX()/zoom));
                    camera.setY((int)(y-me.getY()/zoom));
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e){
            diffX = (int)(e.getX()/zoom) - x;
            diffY = (int)(e.getY()/zoom) - y;
            dragging = true;
        }

    }

}

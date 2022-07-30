package com.mason.libgui.components.panes;

import com.mason.libgui.components.draggables.DraggableComponent;
import com.mason.libgui.core.UIComponent;
import com.mason.libgui.utils.StyleInfo;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;

public class ScrollablePane extends Pane{


    private Camera camera;
    private double maxZoom = 8.0, minZoom = 0.512, zoomFactor = 1.25;


    public ScrollablePane(StyleInfo info, int x, int y, int w, int h, int vx, int vy, int vw, int vh, boolean panLocked){
        super(info, x, y, w, h);
        setCamera(vx, vy, vw, vh, panLocked);
        addComponent(camera);
    }

    public ScrollablePane(StyleInfo info, int x, int y, int w, int h, int vw, int vh, boolean panLocked){
        this(info, x, y, w, h, (vw-w)/2, (vh-h)/2, vw, vh, panLocked);
    }


    public boolean withinBounds(int mx, int my){
        return withinBounds(x, y, camera.getWidth(), camera.getHeight(), mx, my);
    }

    protected boolean inView(UIComponent comp){
        return camera.intersects(comp);
    }

    public void setMinZoom(double minZoom){
        this.minZoom = minZoom;
    }

    public void setMaxZoom(double maxZoom){
        this.maxZoom = maxZoom;
    }

    public void setZoomFactor(double zoomFactor){
        this.zoomFactor = zoomFactor;
    }

    public void setCamera(int vx, int vy, int vw, int vh, boolean panLocked){
        camera = new Camera(vx, vy, vw, vh, panLocked);
    }


    @Override
    protected void renderBorder(Graphics2D g){
        info.RENDER_UTILS.drawBorder(g, StyleInfo.DEFAULT_STYLE_INFO, x, y, camera.getWidth(), camera.getHeight());
    }

    @Override
    protected void renderComponents(Graphics2D g){
        g.setClip(x, y, camera.getWidth(), camera.getHeight());
        AffineTransform saved = g.getTransform();
        g.transform(AffineTransform.getScaleInstance(camera.zoom, camera.zoom));
        g.transform(AffineTransform.getTranslateInstance(x/camera.zoom+camera.getX(), y/camera.zoom+camera.getY()));
        for(int n = componentNum()-1; n>=0; n--){
            /*if(inView(components.get(n))) */getComponent(n).render(g);
        }
        g.setTransform(saved);
        g.setClip(null);
    }

    protected MouseEvent relativeMouseCoords(MouseEvent e){
        return new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiersEx(),
                e.getX() - x - camera.getX(), e.getY() - y - camera.getY(),
                e.getClickCount(), e.isPopupTrigger());
    }

    /*protected MouseWheelEvent relativeMouseCoords(MouseWheelEvent e){
        return new MouseWheelEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiersEx(),
                e.getX() - x - camera.getX(),
                e.getY() - y - camera.getY(), e.getClickCount(), e.isPopupTrigger(), e.getScrollType(), e.getScrollAmount(),
                e.getWheelRotation());
    }*/

    @Override
    public void mousePressed(MouseEvent e){
        super.mousePressed(e);
        if(!isDragging()){
            setDragging(camera);
            e = new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiersEx(), e.getX() - x,
                    e.getY() - y, e.getClickCount(), e.isPopupTrigger());
            camera.mousePressed(e);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e){
        if(camera.isDragging())
            e = new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiersEx(), e.getX() + camera.getX(),
                e.getY() + camera.getY() , e.getClickCount(), e.isPopupTrigger());
        super.mouseDragged(e);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e){
        super.mouseWheelMoved(e);
        camera.mouseWheelMoved(e);
    }


    protected class Camera extends DraggableComponent{


        private final boolean panLocked;
        private double zoom = 1;


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
            if(panLocked) snapToBounds();
        }


        @Override
        public void mouseWheelMoved(MouseWheelEvent me){
            me = relativeMouseCoords(me);
            double xDiff = camera.getX() - me.getX()/zoom, yDiff = camera.getY() - me.getY()/zoom;
            if(me.getWheelRotation() < 0){
                if(zoom < maxZoom){
                    zoom *= zoomFactor;
                    camera.setX((me.getX()/zoom) + xDiff);
                    camera.setY((me.getY()/zoom) + yDiff);
                }
            }else{
                if(zoom > minZoom){
                    zoom /= zoomFactor;
                    camera.setX((me.getX()/zoom) + xDiff);
                    camera.setY((me.getY()/zoom) + yDiff);
                }
            }
            if(panLocked) snapToBounds();
        }

        @Override
        public void mousePressed(MouseEvent e){
            diffX = (int)(e.getX()/zoom) - x;
            diffY = (int)(e.getY()/zoom) - y;
            dragging = true;
        }


        private void setX(double x){
            setX((int)x);
        }

        private void setY(double y){
            setY((int)y);
        }

        private void snapToBounds(){
            if(x < width/camera.zoom-ScrollablePane.this.width) x = (int)((width/camera.zoom-ScrollablePane.this.width));
            else if(x > 0) x = 0;
            if(y > 0) y = 0;
            else if(y < (height/camera.zoom-ScrollablePane.this.height)) y = (int)((height/camera.zoom-ScrollablePane.this.height));
        }

        protected boolean isDragging(){
            return dragging;
        }

    }

}
